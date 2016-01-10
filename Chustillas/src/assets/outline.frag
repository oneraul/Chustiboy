varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;
uniform vec2 u_stepSize;

void main() {
	vec3 outlineColor = vec3(1.0, 0.0, 0.0);

	float alpha = 4.0*texture2D( u_sampler2D, v_texCoord0 ).a;
    alpha -= texture2D(u_sampler2D, v_texCoord0 + vec2( u_stepSize.x, 0.0)).a;
    alpha -= texture2D(u_sampler2D, v_texCoord0 + vec2(-u_stepSize.x, 0.0)).a;
    alpha -= texture2D(u_sampler2D, v_texCoord0 + vec2(0.0,  u_stepSize.y)).a;
    alpha -= texture2D(u_sampler2D, v_texCoord0 + vec2(0.0, -u_stepSize.y)).a;
    
    gl_FragColor = vec4(outlineColor, alpha);
}

