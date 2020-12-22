package com.themaxsmith.tests.springboottest1.demo;

public class Course {

	private String name;
	private double rating;
	private int amtOfRatings;
	
	public Course(String name, double rating, int amtOfRatings) {
		this.name = name;
		this.rating = rating;
		this.amtOfRatings = amtOfRatings;
	}
	
	public String getName() {
		return name;
	}

	public double getRating() {
		return Math.floor(rating*100) / 100;
	}
	
	public void addRating(double score) {
		double newValue = rating * amtOfRatings;
		newValue += score;
		amtOfRatings += 1;
		rating = newValue / amtOfRatings;
	}
}
