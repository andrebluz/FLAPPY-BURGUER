package com.mygdx.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;


public class flappy extends ApplicationAdapter {

	private SpriteBatch batch;

	//Texturas:
	private Texture[] _bird;
	private Texture _bg;
	private Texture canoAlto;
	private Texture canoBaixo;
	private Texture GameOver;

	//Textos:
	BitmapFont textPontuacao;
	BitmapFont textRenicia;
	BitmapFont textMelhorPontuacao;

	private boolean passouCano = false;

	private Random random;

	private int pontuacaoMaxima = 0;
	//score
	private int pontos = 0;
	//gravidade / peso
	private int gravidade = 0;

	private float variacao = 0;
	private float posicaoInicialVertical;
	private float posicaoCanoHorizontal;
	private float posicaoCanoVertical;
	private float larguradispositivo;
	private float alturadispositivo;
	private float espacoEntreCanos;
	private int estadojogo = 0;
	private float posicaoHorizontal = 0;

	private int movimentaX = 0; //movimentação do player eixo X
	private int movimentaY = 0; // || || Y
	private float alturaEnd = 0;
	private float endposicaotela = 0;



	private ShapeRenderer shapeRenderer;
	//  vars de colisão
	private Circle circuloPassaro;
	private Rectangle retaguloCanoCima;
	private Rectangle retanguloBaixo;

	Sound somVoando;
	Sound somColisao;
	Sound somPontuacao;

	Preferences preferencias;

	@Override
	public void create() {
		inicializarObjetos();
		inicializaTexuras();

		alturaEnd = alturadispositivo - posicaoInicialVertical / 2;
		endposicaotela = (larguradispositivo / 2) * 2;

	}

	private void inicializarObjetos() {
		random = new Random();
		batch = new SpriteBatch();

		alturadispositivo = Gdx.graphics.getHeight();
		larguradispositivo = Gdx.graphics.getWidth();
		posicaoInicialVertical = alturadispositivo / 2;
		posicaoCanoHorizontal = larguradispositivo;
		espacoEntreCanos = 350;

		textPontuacao = new BitmapFont();
		textPontuacao.setColor(Color.WHITE);
		textPontuacao.getData().setScale(10);

		textRenicia = new BitmapFont();
		textRenicia.setColor(Color.GREEN);
		textRenicia.getData().setScale(3);

		textMelhorPontuacao = new BitmapFont();
		textMelhorPontuacao.setColor(Color.RED);
		textMelhorPontuacao.getData().setScale(3);

		//colisores:
		circuloPassaro = new Circle();
		retaguloCanoCima = new Rectangle();
		retanguloBaixo = new Rectangle();
		shapeRenderer = new ShapeRenderer();

		somVoando = Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));
		somColisao = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));
		somPontuacao = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));


		preferencias = Gdx.app.getPreferences("FlappyBurguer");
		pontuacaoMaxima = preferencias.getInteger("pontuacaoMaxima", 0);



	}

	private void inicializaTexuras() {

		//texturas
		_bg = new Texture("fundo.png");
		_bird = new Texture[3];
		_bird[0] = new Texture("passaro1.png");
		_bird[1] = new Texture("passaro2.png");
		_bird[2] = new Texture("passaro3.png");
		canoAlto = new Texture("cano_topo_maior.png");
		canoBaixo = new Texture("cano_baixo_maior.png");
		GameOver = new Texture("game_over.png");


	}

	@Override
	public void render() {

		verificaEstadojogo();
		desenharTexturas();
		detectarColisao();
		validarPontos();


	}

	private void detectarColisao() {

		circuloPassaro.set(50 + _bird[0].getWidth() / 2,
				posicaoInicialVertical + _bird[0].getHeight() / 2,
				_bird[0].getWidth() / 2);

		retanguloBaixo.set(posicaoCanoHorizontal, alturadispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical, canoBaixo.getWidth(), canoBaixo.getHeight());

		retaguloCanoCima.set(posicaoCanoHorizontal, alturadispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical, canoAlto.getWidth(), canoAlto.getHeight());

		boolean bateuCanoCima = Intersector.overlaps(circuloPassaro, retaguloCanoCima);
		boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloBaixo);
		if (bateuCanoBaixo || bateuCanoCima) {
			Gdx.app.log("Log", "colideu");

			if (estadojogo == 1) {
				somColisao.play();
				estadojogo = 2;
			}
		}
	}

	private void validarPontos() {
		if (posicaoCanoHorizontal < 50 - _bird[0].getWidth()) {
			if (!passouCano) {
				pontos++;
				passouCano = true;
				somPontuacao.play();
			}

		}
		variacao += Gdx.graphics.getDeltaTime() * 10;

		//animação por frames do burguer
		if (variacao > 3)
		{
			//reseta contagem dos frames
			variacao = 0;
		}

	}

	private void verificaEstadojogo() {

		boolean toqueTela = Gdx.input.justTouched();
		if (estadojogo == 0) {
			if (Gdx.input.justTouched()) {
				gravidade = -15;
				estadojogo = 1;
				somVoando.play();
			}

		} else if (estadojogo == 1) {
			if (toqueTela) {
				gravidade = -15;
				somVoando.play();
			}
			posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 400;
			if (posicaoCanoHorizontal < -canoBaixo.getHeight()) {
				posicaoCanoHorizontal = larguradispositivo;

				//muda altura dos canos
				posicaoCanoVertical = random.nextInt(400) - 200;
				passouCano = false;
			}
			if (posicaoInicialVertical > 0 || toqueTela)
				posicaoInicialVertical = posicaoInicialVertical - gravidade;


			gravidade++;

		} else if (estadojogo == 2) {
			if (pontos > pontuacaoMaxima) {
				pontuacaoMaxima = pontos;
				preferencias.putInteger("pontuacaoMaxima", pontuacaoMaxima);
			}
			posicaoHorizontal -= Gdx.graphics.getDeltaTime() * 500;
			if (toqueTela) {
				estadojogo = 0;
				pontos = 0;
				gravidade = 0;
				posicaoHorizontal = 0;
				posicaoInicialVertical = alturadispositivo / 2;
				posicaoCanoHorizontal = larguradispositivo;
			}
		}


	}

	private void desenharTexturas() {
		batch.begin();
		//renderiza background
		batch.draw(_bg, 0, 0, larguradispositivo, alturadispositivo);
		//renderiza player
		batch.draw(_bird[(int) variacao], 50, posicaoInicialVertical, 92*3, 47*3);
		//renderiza canos
		batch.draw(canoBaixo, posicaoCanoHorizontal, alturadispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical, canoBaixo.getWidth()* 2, canoBaixo.getHeight());
		batch.draw(canoAlto, posicaoCanoHorizontal, alturadispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical, canoAlto.getWidth() * 2, canoAlto.getHeight());
		textPontuacao.draw(batch, String.valueOf(pontos), larguradispositivo / 2, alturadispositivo - 100);

		if (estadojogo == 2) {
			batch.draw(GameOver, larguradispositivo / 2 - GameOver.getWidth() / 2, alturadispositivo / 2);
			textRenicia.draw(batch, "Toque  na tela para reiniciar!", larguradispositivo / 2 - 200, alturadispositivo / 2 - GameOver.getHeight() / 2);
			textMelhorPontuacao.draw(batch, "Sua melhor pontuação  é : " + pontuacaoMaxima + " Pontos", larguradispositivo / 2 - 300, alturadispositivo / 2 - GameOver.getHeight() * 2);
		}


		batch.end();

	}

	@Override
	public void dispose() {

	}


}
