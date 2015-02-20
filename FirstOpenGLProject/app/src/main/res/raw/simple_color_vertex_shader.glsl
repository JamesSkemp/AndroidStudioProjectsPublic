// Current vertex's position
// vec4 is x, y, z, and w coordinates
attribute vec4 a_Position;
attribute vec4 a_Color;
// varying will blend values it receives
varying vec4 v_Color;

void main() {
	v_Color = a_Color;

	// default is 0, 0, 0, 1
	gl_Position = a_Position;
	// The size of points is '10.'
	gl_PointSize = 10.0;
}