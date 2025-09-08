package cms;

import java.util.*;


class Author {
 protected int authorId;
 protected String name;
 protected String email;
 protected String bio;
 protected String role;

 public Author(int authorId, String name, String email, String bio, String role) {
     this.authorId = authorId;
     this.name = name;
     this.email = email;
     this.bio = bio;
     this.role = role;
 }

 public void displayInfo() {
     System.out.println("Author ID: " + authorId + ", Name: " + name + ", Role: " + role);
 }

 
 public void publish(Post post) {
     System.out.println(role + " " + name + " published post: " + post.title);
     post.setStatus("PUBLISHED");
 }
}


class StaffWriter extends Author {
 public StaffWriter(int id, String name, String email, String bio) {
     super(id, name, email, bio, "StaffWriter");
 }

 @Override
 public void publish(Post post) {
     if (post.getStatus().equals("REVIEW")) {
         post.setStatus("PUBLISHED");
         System.out.println("StaffWriter " + name + " published: " + post.title);
     } else {
         System.out.println("StaffWriter can only publish posts under REVIEW.");
     }
 }
}


class GuestAuthor extends Author {
 public GuestAuthor(int id, String name, String email, String bio) {
     super(id, name, email, bio, "GuestAuthor");
 }

 @Override
 public void publish(Post post) {
     System.out.println("Guest authors cannot publish directly. Needs StaffWriter review.");
 }
}


class Category {
 private int categoryId;
 private String name;
 private String description;
 private String parent;

 public Category(int categoryId, String name, String description, String parent) {
     this.categoryId = categoryId;
     this.name = name;
     this.description = description;
     this.parent = parent;
 }

 public String getName() {
     return name;
 }

 public void displayCategory() {
     System.out.println("[" + categoryId + "] " + name + " - " + description);
 }
}


class Post {
 private int postId;
 String title;
 private String content;
 private String status; 
 private List<String> tags;
 private Author author;
 private Category category;

 public Post(int postId, String title, String content, Author author, Category category) {
     this.postId = postId;
     this.title = title;
     this.content = content;
     this.status = "DRAFT";
     this.tags = new ArrayList<>();
     this.author = author;
     this.category = category;
 }

 
 public String getStatus() { return status; }
 public void setStatus(String status) { this.status = status; }
 public Author getAuthor() { return author; }
 public Category getCategory() { return category; }

 
 public void submitForReview() {
     if (status.equals("DRAFT")) {
         status = "REVIEW";
         System.out.println("Post '" + title + "' submitted for REVIEW.");
     }
 }

 public void addTag(String tag) { tags.add(tag); }

 public void displayPost() {
     System.out.println("Post ID: " + postId + ", Title: " + title + ", Status: " + status + ", Author: " + author.name);
 }
}

//----------------- CMS Service Class -----------------
class CMSService {
 private List<Post> posts;
 private List<Category> categories;

 public CMSService() {
     posts = new ArrayList<>();
     categories = new ArrayList<>();
 }


 public void addCategory(Category cat) {
     categories.add(cat);
     System.out.println("Category added: " + cat.getName());
 }

 
 public void createPost(Post post) {
     posts.add(post);
     System.out.println("Post created: " + post.title);
 }

 
 public void search(String title) {
     System.out.println("Searching posts by title: " + title);
     for (Post p : posts) {
         if (p.title.equalsIgnoreCase(title)) p.displayPost();
     }
 }

 
 public void searchByTag(String tag) {
     System.out.println("Searching posts by tag: " + tag);
     for (Post p : posts) {
         if (p.title.contains(tag)) p.displayPost();
     }
 }

 
 public void listByCategory(String categoryName) {
     System.out.println("Posts under category: " + categoryName);
     for (Post p : posts) {
         if (p.getCategory().getName().equalsIgnoreCase(categoryName)) {
             p.displayPost();
         }
     }
 }
 
 public void authorWiseCount() {
     Map<String, Integer> countMap = new HashMap<>();
     for (Post p : posts) {
         String authorName = p.getAuthor().name;
         countMap.put(authorName, countMap.getOrDefault(authorName, 0) + 1);
     }
     System.out.println("Author-wise post count:");
     for (String a : countMap.keySet()) {
         System.out.println(a + ": " + countMap.get(a));
     }
 }
}


public class CMSAppMain {
 public static void main(String[] args) {
     CMSService service = new CMSService();

    
     StaffWriter staff = new StaffWriter(1, "Alice", "alice@mail.com", "Tech Writer");
     GuestAuthor guest = new GuestAuthor(2, "Bob", "bob@mail.com", "Guest Blogger");

    
     Category tech = new Category(101, "Technology", "Tech Articles", "Root");
     Category food = new Category(102, "Food", "Food Blogs", "Root");
     service.addCategory(tech);
     service.addCategory(food);

     
     Post p1 = new Post(201, "AI in 2025", "Future of AI", staff, tech);
     Post p2 = new Post(202, "Best Pizza", "Food review", guest, food);

     service.createPost(p1);
     service.createPost(p2);

     
     p1.submitForReview();
     staff.publish(p1);   
     guest.publish(p2);   

    
     service.search("AI in 2025");
     service.listByCategory("Technology");
     service.authorWiseCount();
 }
}