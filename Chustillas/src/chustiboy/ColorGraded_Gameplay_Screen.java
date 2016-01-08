package chustiboy;
import chustiboy.net.Network;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ColorGraded_Gameplay_Screen extends Gameplay_Screen {
	
	private Texture LUT;
	private ShaderProgram shader;
	
	public ColorGraded_Gameplay_Screen(Network net) {
		super(net);
		
		ShaderProgram.pedantic = false;
		String VERTEX = Gdx.files.internal("assets/colorGrading.vert").readString();
		String FRAGMENT = Gdx.files.internal("assets/colorGrading.frag").readString();
		shader = new ShaderProgram(VERTEX, FRAGMENT);
		
		// TODO solo para debug
      	if(!shader.isCompiled())
      		throw new GdxRuntimeException(shader.getLog());
      	if(shader.getLog().length() != 0)
      		System.out.println(shader.getLog());
			
		shader.begin();
		shader.setUniformi("u_lut", 1);
		shader.setUniformMatrix("u_worldView", batch.getProjectionMatrix());
		shader.end();

		fboBatch.setShader(shader);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		shader.begin();
		shader.setUniformMatrix("u_worldView", fboBatch.getProjectionMatrix());
		shader.end();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		LUT.dispose();
		shader.dispose();
	}
	
	protected void setLUT(Texture lut) {
		this.LUT = lut;
		LUT.bind(1);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
	}
}
