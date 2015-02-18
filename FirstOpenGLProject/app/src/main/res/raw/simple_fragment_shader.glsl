precision mediump float;

// uniform keeps the same value until we choose another
uniform vec4 u_Color;

void main() {
	// red, green, blue, alpha
	gl_FragColor = u_Color;
}