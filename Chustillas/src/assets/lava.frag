#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoord0;

// http://rotatingcanvas.com/fragment-shader-to-simulate-water-surface-in-libgdx/

uniform sampler2D u_sampler2D;
uniform sampler2D u_noise;
uniform float u_deltatime;

void main() {                                            
	float displacement = texture2D(u_noise, v_texCoord0/6.0).y;
	displacement = displacement*0.1-0.15 + (sin(v_texCoord0.x * 60.0+u_deltatime) * 0.005);
	
	float x = v_texCoord0.x;
	if(v_texCoord0.x < 0.5) 
		 x += displacement;
	else x -= displacement;

	float y = v_texCoord0.y;
	if(v_texCoord0.y < 0.5) 
		 y += displacement;
	else y -= displacement;	
	
	gl_FragColor = v_color * texture2D(u_sampler2D, vec2(x, y));
}