package vttp2023.batch3.csf.assessment.cnserver.services;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vttp2023.batch3.csf.assessment.cnserver.models.News;
import vttp2023.batch3.csf.assessment.cnserver.models.TagCount;
import vttp2023.batch3.csf.assessment.cnserver.repositories.ImageRepository;
import vttp2023.batch3.csf.assessment.cnserver.repositories.NewsRepository;


@Service
public class NewsService {
	
	@Autowired
	private NewsRepository newsRepo;

	@Autowired
	private ImageRepository imageRepo;

	// TODO: Task 1
	// Do not change the method name and the return type
	// You may add any number of parameters
	// Returns the news id
	public String postNews(/* Any number of parameters */ Document news, MultipartFile file) {

		String id = this.newsRepo.insertNews(news).getString("_id");
		this.imageRepo.uploadFile(file);
		return id;
	}

	public String getUrl(String fileName) {

		return this.imageRepo.getURL(fileName);
	}
	 
	// TODO: Task 2
	// Do not change the method name and the return type
	// You may add any number of parameters
	// Returns a list of tags and their associated count
	public List<TagCount> getTags(/* Any number of parameters */ int minutes) {

		List<Document> tags = this.newsRepo.getNewsByTime(minutes);

		List<TagCount> tagCount = tags.stream()
			.map(value -> {
				return new TagCount(value.getString("tag"), value.getInteger("count"));
			})
			.collect(Collectors.toList());


		return tagCount;
	}

	// TODO: Task 3
	// Do not change the method name and the return type
	// You may add any number of parameters
	// Returns a list of news
	public List<News> getNewsByTag(/* Any number of parameters */int time, String tag) {

		List<News> result = this.newsRepo.getNewsByTag(time, tag).stream()
			.map(doc -> {
				News news =	new News();
				news.setId(doc.getString("_id"));
				news.setPostDate(doc.getLong("postDate"));
				news.setDescription(doc.getString("description"));
				news.setImage(doc.getString("image"));
				news.setTags(doc.getList("tag", String.class));
				news.setTitle(doc.getString("title"));
				return news;
			})
			.collect(Collectors.toList());

		return result;
	}
	
}
