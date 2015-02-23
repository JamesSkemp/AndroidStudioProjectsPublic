package com.jamesrskemp.firstopenglproject.util;

import android.util.FloatMath;

/**
 * Created by James on 2/22/2015.
 */
public class Geometry {
	/**
	 * 2D circle.
	 */
	public static class Circle {
		public final Point center;
		public final float radius;

		public Circle(Point center, float radius) {
			this.center = center;
			this.radius = radius;
		}

		public Circle scale(float scale) {
			return new Circle(center, radius * scale);
		}
	}

	/**
	 * 3D cylinder.
	 */
	public static class Cylinder {
		public final Point center;
		public final float radius;
		public final float height;

		public Cylinder(Point center, float radius, float height) {
			this.center = center;
			this.radius = radius;
			this.height = height;
		}
	}

	/**
	 * Point in 3D space.
	 */
	public static class Point {
		public final float x, y, z;

		public Point(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public Point translate(Vector vector) {
			return new Point(
					x + vector.x,
					y + vector.y,
					z + vector.z
			);
		}

		public Point translateY(float distance) {
			return new Point(x, y + distance, z);
		}
	}

	/**
	 * Sphere in 3D space.
	 */
	public static class Sphere {
		public final Point center;
		public final float radius;

		public Sphere(Point center, float radius) {
			this.center = center;
			this.radius = radius;
		}
	}

	/**
	 * Ray from the near plane to the far plane.
	 */
	public static class Ray {
		public final Point point;
		public final Vector vector;

		/**
		 * Construct a Ray from a point in a particular direction.
		 * @param point Starting point of the ray.
		 * @param vector Direction of the ray.
		 */
		public Ray(Point point, Vector vector) {
			this.point = point;
			this.vector = vector;
		}
	}

	/**
	 * Directional coordinates.
	 */
	public static class Vector {
		public final float x, y, z;

		public Vector(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		/**
		 * Get the length of a vector.
		 * @return Length based on Pythagoras' theorem.
		 */
		public float length() {
			return FloatMath.sqrt(x * x + y * y + z * z);
		}

		/**
		 * Gets the cross product between two vectors.
		 * @param other Vector to perform the cross product with.
		 * @return Vector perpendicular to both.
		 */
		public Vector crossProduct(Vector other) {
			return new Vector(
					(y * other.z) - (z * other.y),
					(z * other.x) - (x * other.z),
					(x * other.y) - (y * other.x)
			);
		}
	}

	/**
	 * Determine whether a Ray and Sphere intersect.
	 * @param sphere Sphere, potentially containing an object.
	 * @param ray Ray the Sphere must be within.
	 * @return True if the Ray and Sphere intersect.
	 */
	public static boolean intersects(Sphere sphere, Ray ray) {
		return distanceBetween(sphere.center, ray) < sphere.radius;
	}

	/**
	 * Returns a direction from one Point in space to another.
	 * @param from Point to start from.
	 * @param to Point to go to.
	 * @return Direction.
	 */
	public static Vector vectorBetween(Point from, Point to) {
		return new Vector(
				to.x - from.x,
				to.y - from.y,
				to.z - from.z
		);
	}

	public static float distanceBetween(Point point, Ray ray) {
		Vector p1ToPoint = vectorBetween(ray.point, point);
		Vector p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point);

		// No clue. But supposedly there's a parallelogram between the two vectors, which can be thought of as two triangles.
		float areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length();
		float lengthOfBase = ray.vector.length();

		// The height of the triangle is the distance from the point to the ray.
		float distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase;

		return distanceFromPointToRay;
	}
}
