varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

void main() {
	vec4 pixel = texture2D(u_sampler2D, v_texCoord0);
	
	if(pixel.a == 0.0) 
		discard;
	
	gl_FragColor = vec4(1.0, 0.0, 0.0, pixel.a);
}
