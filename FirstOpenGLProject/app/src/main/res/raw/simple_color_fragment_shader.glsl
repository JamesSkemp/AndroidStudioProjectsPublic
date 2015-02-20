precision mediump float;
// varying will blend the colors it receives
varying vec4 v_Color;

void main() {
	// red, green, blue, alpha
	gl_FragColor = v_Color;
}