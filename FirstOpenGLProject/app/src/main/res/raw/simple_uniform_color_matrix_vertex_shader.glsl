// a 4x4 matrix
uniform mat4 u_Matrix;

// Current vertex's position
// vec4 is x, y, z, and w coordinates
attribute vec4 a_Position;

void main() {
	// default is 0, 0, 0, 1
	// interpret as existing in a virtual coordinate space
	gl_Position = u_Matrix * a_Position;
}