package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class flappy extends ApplicationAdapter {

	private SpriteBatch batch;

	private Texture[] _bird;
	private Texture _bg;

	private Texture[] canoAlto;
	private Texture[] canoBaixo;


	private float larguradispositivo; //largura total da tela
	private float alturadispositivo; //altura    ||  ||

	private int movimentaX = 0; //movimentação do player eixo X
	private int movimentaY = 0; // || || Y

	private float variacao = 0;
	private float gravidade = 0; //gravidade para gerenciar o salto
	private float posicaoInicialVertical = 0;

	private float alturaEnd = 0;
	private float endposicaotela = 0;

	private int tempTubo = 0;

	@Override
	public void create() {

		batch = new SpriteBatch();
		_bg = new Texture("fundo.png"); //background

		//animação do pássaro por array
		_bird = new Texture[3];
		_bird[0] = new Texture("passaro1.png");
		_bird[1] = new Texture("passaro2.png");
		_bird[2] = new Texture("passaro3.png");

		//img dos tubos
		canoAlto = new Texture[2];
		canoAlto[0] = new Texture("cano_topo.png");
		canoAlto[1] = new Texture("cano_topo_maior.png");
		canoBaixo = new Texture[2];
		canoBaixo[0] = new Texture("cano_baixo.png");
		canoBaixo[1] = new Texture("cano_baixo_maior.png");


		alturadispositivo = Gdx.graphics.getHeight();
		larguradispositivo = Gdx.graphics.getWidth();

		posicaoInicialVertical = alturadispositivo / 2;
		alturaEnd = alturadispositivo - posicaoInicialVertical / 2;
		endposicaotela = (larguradispositivo / 2) * 2;


	}

	@Override
	public void render() {
		batch.begin();

		//animação do pássaro
		if (variacao > 3)
		{
			variacao = 0;
		}

		boolean toqueTela = Gdx.input.justTouched();
		if (Gdx.input.justTouched()) {
			gravidade = -25;}


		if (posicaoInicialVertical > 0 || toqueTela) {
			posicaoInicialVertical = posicaoInicialVertical - gravidade;
			/**tempTubo++;
			if (tempTubo <= 0){
				tuboMario();

			}**/
		}

		batch.draw(_bg, 0, 0, larguradispositivo, alturadispositivo);

		batch.draw(_bird[(int) variacao], 50, posicaoInicialVertical, 92*3, 47*3);
		variacao += Gdx.graphics.getDeltaTime() * 5;

		gravidade++;
		movimentaY++;
		movimentaX++;


		//if (tempTubo <= 0){
			tuboMario();

		//}

		batch.end();



	}
	void tuboMario() {
		batch.draw(canoAlto[0], endposicaotela - movimentaX, alturaEnd - 50, 100*3, 900*3);
		batch.draw(canoBaixo[0], endposicaotela - movimentaX, -1910, 100*3, 900*3);
	}

	@Override
	public void dispose() {

	}

}

