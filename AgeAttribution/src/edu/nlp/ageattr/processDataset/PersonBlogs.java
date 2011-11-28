package edu.nlp.ageattr.processDataset;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Each object will hold information about a person and his blogs
 * @author piyush
 *
 */
public class PersonBlogs {
private int bloggerId;
private int bloggerAge;
private int bloggerGender;
private ArrayList<String> posts;
/**
 * consructor to create the personBlogs object
 * @param BloggerId
 * @param BloggerAge
 * @param BloggerGender
 * @param Posts
 */
public PersonBlogs(int BloggerId,int BloggerAge,int BloggerGender,ArrayList<String> Posts){
	bloggerAge = BloggerAge;
	bloggerGender = BloggerGender;
	bloggerId = BloggerId;
	posts = new ArrayList<String>();
	posts = Posts;
}

/**
 * @return the bloggerId
 */
public int getBloggerId() {
	return bloggerId;
}

/**
 * @param bloggerId the bloggerId to set
 */
public void setBloggerId(int bloggerId) {
	this.bloggerId = bloggerId;
}

/**
 * @return the bloggerAge
 */
public int getBloggerAge() {
	return bloggerAge;
}

/**
 * @param bloggerAge the bloggerAge to set
 */
public void setBloggerAge(int bloggerAge) {
	this.bloggerAge = bloggerAge;
}

/**
 * @return the bloggerGender
 */
public int getBloggerGender() {
	return bloggerGender;
}

/**
 * @param bloggerGender the bloggerGender to set
 */
public void setBloggerGender(int bloggerGender) {
	this.bloggerGender = bloggerGender;
}

/**
 * @return the posts
 */
public ArrayList<String> getPosts() {
	return posts;
}

/**
 * @param posts the posts to set
 */
public void setPosts(ArrayList<String> posts) {
	this.posts = posts;
}

/**
 * prints a personBlog info
 * @param personBlogs
 */
public void printPersonInfo(PersonBlogs personBlogs){
	System.out.println("Info for blogger with Id "+personBlogs.bloggerId);
	System.out.println("Blogger Age "+personBlogs.bloggerAge);
	System.out.println("Blogger Gender "+personBlogs.bloggerGender);
	System.out.println("Posts : ");
	for(int i=0;i<personBlogs.posts.size();i++){
		System.out.println("Post no "+(i+1)+" : "+personBlogs.posts.get(i));
	}
}
/**
 * returns a group name according to a persons age
 * @return
 */
public String getBloggerAgeClass() {
	if(this.getBloggerAge()>=13 && this.getBloggerAge()<=17){
		return "teens";
	}
	else if(this.getBloggerAge()>=23 && this.getBloggerAge()<=27){
		return "twenties";
	}
	else{
		return "thirtees";
	}
}

}
