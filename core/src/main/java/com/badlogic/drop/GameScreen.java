package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
	final Drop game;

    /* Declaração dos assets necessários. */
    Texture bucketTexture;
    Texture dropTexture;

    /* Som e música */
    Sound dropSound;
	Sound errorSound;
    Music music;

    /* Define o movimento do balde. */
    Sprite bucketSprite; // Declare a new Sprite variable

    /* Para cálculo de coordenadas. */
    Vector2 touchPos;

    /* Lista das gotas de chuva. */
    Array<Sprite> dropSprites;

    /* DElay entre uma gota e outra. */
    float dropTimer;

    /* Retângulos para comparar colisão entre gotas e o balde. */
    Rectangle bucketRectangle;
    Rectangle dropRectangle;

    /* Marcar os pontos. */
    private int pontos;

	/* Marcar os erros. */
    private int erros;

    public GameScreen(final Drop game) {
		this.game = game;
        // Prepare your application here.
        /* Inicialização das imagens */
        bucketTexture = new Texture("bucket.png");
        dropTexture = new Texture("drop.png");

        /* Inicialização dos sons e música. */
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
		errorSound = Gdx.audio.newSound(Gdx.files.internal("error.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

        /* Inicialização do movimento do balde. */
        bucketSprite = new Sprite(bucketTexture); // Initialize the sprite based on the texture
        bucketSprite.setSize(1, 1); // Define the size of the sprite

        /* Inicialização do vetor de coordenadas. */
        touchPos = new Vector2();

        /* Inicialização da lista de gotas. */
        dropSprites = new Array<>();

        /* Inicialização dos retângulos de comparação. */
        bucketRectangle = new Rectangle();
        dropRectangle = new Rectangle();

        /* Para tocar a música em loop no inicio do jogo. */
        music.setLooping(true);
        music.setVolume(.4f);
        music.play();

        pontos = 0; // Inicializa a pontuação
		erros = 0; // Inicializa o erro
		// Aumentar o tamanho da fonte com escala
        this.game.font.getData().setScale(2.0f); // Dobrar o tamanho da fonte
    }

    @Override
    public void resize(int width, int height) {
        // Resize your application here. The parameters represent the new window size.
        this.game.viewport.update(width, height, true); // true centers the camera
    }

	@Override
	public void render(float delta) {
		// Chame input, logic e draw com o delta
		input();
		logic();
		draw();
	}
	

    private void input() {
        /* Velociadade do movimento. */
        float speed = 4f;

        /* Garantir que o movimento seja o mesmo independente de hardware. */
        float delta = Gdx.graphics.getDeltaTime(); // retrieve the current delta

        /* Faz alguma coisa quando a tecla é precionada. */
        /* Tecla para a direita. */
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucketSprite.translateX(speed * delta); // Move the bucket right
        }
        /* Tecla para a esquerda. */
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucketSprite.translateX(-speed * delta); // move the bucket left
        }

        /* Faz alguma coisa quando a tecla é precionada. */
        /* Tecla para cima. */
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            bucketSprite.translateY(speed * delta); // Move the bucket right
        }
        /* Tecla para baixo. */
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            bucketSprite.translateY(-speed * delta); // move the bucket left
        }

        /* Faz alguma coisa quando clica ou toca a tela. */
        if (Gdx.input.isTouched()) { // If the user has clicked or tapped the screen
            /* Pega onde o toque aconteceu na tela. */
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); 
            /* Converte as unidades da janela para as coordenadas do cenário. */
            this.game.viewport.unproject(touchPos);
            /* Muda a posição do centro do balde horizontalmente. */
            bucketSprite.setCenterX(touchPos.x); // Change the horizontally centered position of the bucket
        }
    }
    
    private void logic() {
        /* Pega os valores do tamanha do cenário. */
        float worldWidth = this.game.viewport.getWorldWidth();
        float worldHeight = this.game.viewport.getWorldHeight();

        /* Pega as coordenadas do balde brevemente. */
        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();

        /* Define que a posição do balde é um valor entre 0 e o tamanha da largura do cenário. */
        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth - bucketWidth));

        /* Define que a posição do balde é um valor entre 0 e o tamanha da altura do cenário. */
        bucketSprite.setY(MathUtils.clamp(bucketSprite.getY(), 0, worldHeight - bucketHeight));

        float delta = Gdx.graphics.getDeltaTime(); // retrieve the current delta

        /* Aplica as coordenadas do balde para o retângulo do balde. */
        bucketRectangle.set(bucketSprite.getX(), bucketSprite.getY(), bucketWidth, bucketHeight);


        /* Percorre a lista de gotas removendo a última gota que caiu para que não ocorra erro de memória. */
        for (int i = dropSprites.size - 1; i >= 0; i--) {
            Sprite dropSprite = dropSprites.get(i); // Get the sprite from the list
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();
            
            /* Para cada gota ele movimenta ela para baixo. */
			float dropSpeed = 2f + (pontos / 20f); // Aumenta a velocidade a cada 20 pontos
			dropSprite.translateY(-dropSpeed * delta);

            /* Aplica as coordenadas da gota para o retângulo da gota. */
            dropRectangle.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);

            /* Remove a gota se ela tiver passado direto pela tela. */
            if (dropSprite.getY() < 0) {
				
				/* Para fazer um som com quando a gota passa direto. */
				dropSprites.removeIndex(i);
				errorSound.setVolume(2, .7f);
                errorSound.play(); // Play the sound
				erros++;
			}

            /* Remove a gota se ela colidir com o balde. */
            else if (bucketRectangle.overlaps(dropRectangle)){
                dropSprites.removeIndex(i); // Remove the drop

                /* Para fazer um som com quando a gota encosta no balde. */
				dropSound.setVolume(1, .5f);
                dropSound.play(); // Play the sound
                pontos++;
            }
        }

        dropTimer += delta; // Adds the current delta to the timer
        if (dropTimer > 1f) { // Check if it has been more than a second
            dropTimer = 0; // Reset the timer
            /* Função que cria uma gota. */
            createDroplet();
        }

		if (erros >= 10) {
			this.game.setScreen(new GameOver(game));
			dispose();
		}

		else if (pontos >= 50){
			this.game.setScreen(new GameWin(game));
			dispose();
		}

    }
    
    private void draw() {
        /* Limpa a tela com alguma cor*/
        ScreenUtils.clear(Color.WHITE);
        this.game.viewport.apply();

        /* Mostra como o draw é aplicado na janela */
        this.game.batch.setProjectionMatrix(this.game.viewport.getCamera().combined);
        
        /* Começa o desenho da tela. */
		this.game.batch.begin();

            /* Definindo a altura e a largura do fundo como iguais a da janela. */
            float worldWidth = this.game.viewport.getWorldWidth();
            float worldHeight = this.game.viewport.getWorldHeight();

            /* Desenha o fundo. */
            this.game.batch.draw(this.game.backgroundTexture, 0, 0, worldWidth, worldHeight); // draw the background
            /* Desenha o balde. */
            bucketSprite.draw(this.game.batch); // Sprites have their own draw method

            /* Desenha cada gota. */
            for (Sprite dropSprite : dropSprites) {
                dropSprite.draw(this.game.batch);
            }

        /* Termina o desenho da tela. */
        this.game.batch.end();

        // Começa a desenhar
        this.game.Fontbatch.begin();
        
        this.game.font.draw(this.game.Fontbatch, "Pontos: " + pontos, 5, 490); 
		this.game.font.draw(this.game.Fontbatch, "Erros: " + erros, 5, 450);         
        
        // Termina o desenho
        this.game.Fontbatch.end();
    }

    private void createDroplet() {
        // create local variables for convenience
        float dropWidth = 0.8f;
        float dropHeight = 0.8f;
        float worldWidth = this.game.viewport.getWorldWidth();
        float worldHeight = this.game.viewport.getWorldHeight();
        
        // create the drop sprite
        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth, dropHeight);
        dropSprite.setX(MathUtils.random(0f, worldWidth - dropWidth)); // Randomize the drop's x position
        dropSprite.setY(worldHeight);
        dropSprites.add(dropSprite); // Add it to the list

    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
	public void dispose() {
		this.bucketTexture.dispose();
		this.dropTexture.dispose();
		this.dropSound.dispose();
		this.music.dispose();
	}

	@Override
	public void show() {
		/* */
	}

	@Override
	public void hide() {
		/* */
	}
}