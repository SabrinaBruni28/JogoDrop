package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

	final Drop game;
  
	public MainMenuScreen(final Drop game) {
		this.game = game;
        // Aumentar o tamanho da fonte com escala
        this.game.font.getData().setScale(3.0f); // Dobrar o tamanho da fonte
	}

    @Override
	public void render(float delta) {
        draw();
		input();
        logic();
	}

    private void input() {
        /* Faz alguma coisa quando a tela é tocada. */
        if (Gdx.input.isTouched()) {
			this.game.setScreen(new GameScreen(game));
			dispose();
		}
    }

    private void logic() {
      /* */  

    }

    private void draw() {
        ScreenUtils.clear(Color.WHITE);
        this.game.viewport.apply();
    
        // Define a matriz de projeção do SpriteBatch
        this.game.batch.setProjectionMatrix(this.game.viewport.getCamera().combined);
    
        // Inicia o desenho
        this.game.batch.begin(); // Use o spriteBatch, não game.batch
    
            // Pega os valores do tamanho do cenário
            float worldWidth = this.game.viewport.getWorldWidth();
            float worldHeight = this.game.viewport.getWorldHeight();
            
            // Desenha o fundo
            this.game.batch.draw(this.game.backgroundTexture, 0, 0, worldWidth, worldHeight);
    
        // Termina o desenho
        this.game.batch.end();

        this.game.Fontbatch.begin();
            // Desenha o texto
            game.font.draw(game.Fontbatch, "Welcome to Drop!!! ", 150, 200);
            game.font.draw(game.Fontbatch, "Tap anywhere to begin!", 150, 150);
            
        this.game.Fontbatch.end();
    }
    

    @Override
    public void show() {
        /* */
    }

    @Override
    public void resize(int width, int height) {
        // Resize your application here. The parameters represent the new window size.
        this.game.viewport.update(width, height, true); // true centers the camera
    
    }

    @Override
    public void pause() {
        /* */
    }

    @Override
    public void resume() {
       /* */
    }

    @Override
    public void hide() {
        /* */
    }

    @Override
    public void dispose() {
        /* */
    }
}
