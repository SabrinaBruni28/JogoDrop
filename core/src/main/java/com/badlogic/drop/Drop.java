package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class Drop extends Game {

	public SpriteBatch batch;
	public SpriteBatch Fontbatch;
	public BitmapFont font;
	/* Define as proporções da janela. */
    FitViewport viewport;
	/* Imagens */
    Texture backgroundTexture;

	@Override
	public void create() {
		batch = new SpriteBatch();
		Fontbatch = new SpriteBatch();
		font = new BitmapFont(); // use libGDX's default Arial font
		viewport = new FitViewport(8, 5);
		viewport.getCamera().position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0); // Centraliza a câmera
		/* Inicialização das imagens */
        backgroundTexture = new Texture("background.png");

		// Alterando a cor da fonte
        font.setColor(Color.WHITE);

		this.setScreen(new MainMenuScreen(this));
	}


	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		this.batch.dispose();
		this.font.dispose();
		this.backgroundTexture.dispose();
		this.Fontbatch.dispose();
	}

}
