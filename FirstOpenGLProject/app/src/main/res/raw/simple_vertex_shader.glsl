// Current vertex's position
attribute vec4 a_Position;

void main() {
	// vec4 is x, y, z, and w coordinates
	// default is 0, 0, 0, 1
	gl_Position = a_Position;
	// The size of points is '10.'
	gl_PointSize = 10.0;
}