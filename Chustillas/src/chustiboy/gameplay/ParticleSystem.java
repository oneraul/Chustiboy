package chustiboy.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import chustiboy.Assets;
import chustiboy.gameplay.Pool.Poolable;

public class ParticleSystem {
	private Pool<Particle> particles;
	private int width, height;
	private float max_particle_life, initial_particle_size, final_particle_size, particle_v;
	private int max_particles,emission_rate;
	private Color[] colors;
	public float x, y;
	public boolean emiting;
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void draw(SpriteBatch batch) {
		update();

		// older particles are drawn first
		for(int i = particles.active.size-1; i >= 0; i--) {
			particles.active.get(i).draw(batch);
		}
	}
	
	public void resetParticles() {
		particles.freeAll();
	}

	private void update() {
		float dt = Gdx.graphics.getDeltaTime();
		for(int i = particles.active.size-1; i >= 0; i--) {
			Particle particle = particles.active.get(i);
			if(particle.update(dt)) {
				particles.free(particle);
			}
		}

		if(emiting) renewParticles();
	}

	private void renewParticles() {
		int particles_to_spawn = Math.abs(MathUtils.round(MathUtils.randomTriangular(emission_rate)));
		particles_to_spawn = Math.min(particles_to_spawn, max_particles-particles.active.size);
		
		for(int i = 0; i < particles_to_spawn; i++) emit();
	}

	private void emit() {
		Particle p = particles.obtain();

		float x = this.x + MathUtils.random(-width/2, width/2);
		float y = this.y + MathUtils.random(height);
		p.setCenterX(x);
		p.setY(y);

		int color = MathUtils.random(colors.length-1);

		// TODO this is a hack for the fire, with other colors number will crash
		if(color == 2) color = MathUtils.random(colors.length-1);
		if(color == 2) color = MathUtils.random(colors.length-1);

		p.setColor(colors[color]);
		p.v 			   = particle_v;
		p.initial_size 	   = initial_particle_size;
		p.final_size 	   = final_particle_size;
		p.initial_lifetime = p.lifetime = MathUtils.random(max_particle_life/2, max_particle_life);
		p.setScale(initial_particle_size);
	}

	// BUILDER ------------------
	public static class Builder {

		// Required parameters
		private final float x, y;
		private final int width, height;

		// Optional parameters - initialize with default values
		private float max_particle_life, initial_particle_size, final_particle_size, particle_v;
		private int max_particles, emission_rate, base_particle_size;
		private Color[] colors;
		private Texture texture;

		public Builder(float x, float y, int width, int height) {
			this.x      = x;
			this.y      = y;
			this.width  = width;
			this.height = height;

			// default values
			emission_rate         = 5;
			max_particles         = width * height * 3;
			max_particle_life     = 1f;
			initial_particle_size = 5f;
			final_particle_size   = 0;
			particle_v            = 1f;
			colors                = new Color[] {Color.RED, Color.ORANGE, Color.YELLOW};
			texture               = Assets.whitePixel;
			base_particle_size    = 1;
		}
		
		public Builder texture(Texture texture) {
			this.texture = texture;
			return this;
		}
		
		public Builder base_particle_size(int size) {
			this.base_particle_size = size;
			return this;
		}

		public Builder emission_rate(int rate) {
			emission_rate = rate;
			return this;
		}

		public Builder max_particles(int max) {
			max_particles = max;
			return this;
		}

		public Builder max_particle_life(float seconds) {
			max_particle_life = seconds;
			return this;
		}

		public Builder initial_particle_size(float size) {
			initial_particle_size = size;
			return this;
		}
		
		public Builder final_particle_size(float size) {
			final_particle_size = size;
			return this;
		}

		public Builder particle_v(float v) {
			particle_v = v;
			return this;
		}

		public Builder colors(Color... colors) {
			this.colors = colors;
			return this;
		}

		public ParticleSystem build() {
			return new ParticleSystem(this);
		}
	}

	private ParticleSystem(final Builder builder) {
		
		// required"
		x      = builder.x;
		y      = builder.y;
		width  = builder.width;
		height = builder.height;

		// optional
		emission_rate = builder.emission_rate;
		max_particles = builder.max_particles;
		max_particle_life = builder.max_particle_life;
		initial_particle_size = builder.initial_particle_size;
		final_particle_size = builder.final_particle_size;
		particle_v = builder.particle_v;
		colors = builder.colors;

		//internal
		emiting = true;
		particles = new Pool<Particle>(max_particles, false) {
			@Override
			public Particle newItem() {
				if(Gdx.app.getType() == ApplicationType.Android)
					return new Particle();
				return new Particle(builder.texture, builder.base_particle_size);
			}
		};
	}
	
	// PARTICLE -----------------
	public class Particle extends Sprite implements Poolable {
		private float initial_lifetime, lifetime, initial_size, final_size, v;

		Particle(Texture texture, int baseParticleSize) {
			super(texture);
			this.setSize(baseParticleSize, baseParticleSize);
			this.setOrigin(this.getWidth()/2, 0);
		}
		
		Particle() {
			this(Assets.whitePixel, 1);
		}

		private boolean update(float dt) {
			if((lifetime -= dt) <= 0) {
				return true;
			}

			doStuff();
			return false;
		}

		// this is the method that should be overriden to customize particle behaviour
		public void doStuff() {
			setScale(MathUtils.lerp(final_size, initial_size, lifetime / initial_lifetime));
			translateY(v);
		}

		@Override
		public void reset() {}
	}
}
