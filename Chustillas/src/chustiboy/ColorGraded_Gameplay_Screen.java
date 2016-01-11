package chustiboy;
import chustiboy.net.Network;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ColorGraded_Gameplay_Screen extends Gameplay_Screen {
	
	private Texture LUT;
	
	public ColorGraded_Gameplay_Screen(Network net) {
		super(net);

		String passthroughVert  = Gdx.files.internal("assets/passthrough.vert").readString();
		String colorGradingFrag = Gdx.files.internal("assets/colorGrading.frag").readString();
		sceneShader = new ShaderProgram(passthroughVert, colorGradingFrag);
		
		// TODO solo para debug
      	if(!sceneShader.isCompiled())
      		throw new GdxRuntimeException(sceneShader.getLog());
      	if(sceneShader.getLog().length() != 0)
      		System.out.println(sceneShader.getLog());
			
      	sceneShader.begin();
      	sceneShader.setUniformi("u_lut", 1);
      	sceneShader.end();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		LUT.dispose();
	}
	
	protected void setLUT(Texture lut) {
		this.LUT = lut;
		LUT.bind(1);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
	}
}
