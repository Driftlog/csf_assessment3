package vttp2023.batch3.csf.assessment.cnserver.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.Response;
import com.amazonaws.services.kms.model.Tag;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2023.batch3.csf.assessment.cnserver.models.News;
import vttp2023.batch3.csf.assessment.cnserver.models.TagCount;
import vttp2023.batch3.csf.assessment.cnserver.services.NewsService;

@RestController
@RequestMapping()
public class NewsController {

	@Autowired
	private NewsService svc;


	// TODO: Task 1
	@PostMapping( path="/postnews",consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> postNews(@RequestPart MultipartFile image, @RequestPart String title, @RequestPart String description,
											@RequestPart String tags) {

		String url = this.svc.getUrl(image.getOriginalFilename());										

		Document doc = new Document()
						.append("_id", new ObjectId())
						.append("postDate", System.currentTimeMillis())
						.append("title", title)
						.append("description", description)
						.append("image", url);

		if (tags.length() > 0) {

			try (InputStream is = new ByteArrayInputStream(tags.getBytes())) {
				JsonReader reader = Json.createReader(is);
				JsonArray arr = reader.readArray();

				doc.append("tag", arr);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
 		
		String newsId = this.svc.postNews(doc, image);

		JsonObject obj = Json.createObjectBuilder()
								.add("newsId", newsId)
								.build();

		return ResponseEntity.ok(obj.toString());
	}

	// TODO: Task 2

	@GetMapping(path="/getTags", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getTags(@RequestParam int time) {

		List<TagCount> tags = this.svc.getTags(time);

		if (tags.isEmpty ()) {

			JsonObject respObj = Json.createObjectBuilder()
									.add("tags", "Nil")
									.build();

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respObj.toString());

		}

		JsonObject respObj = Json.createObjectBuilder()
								.add("tags", tags.toString())
								.build();

		return ResponseEntity.ok(respObj.toString());
	}

	// TODO: Task 3
	@GetMapping()
	public ResponseEntity<String> getNews(@RequestParam int time, @RequestParam String tag) {

		List<News> newsList = this.svc.getNewsByTag(time, tag);

		if (newsList.isEmpty()) {

			JsonObject respObj = Json.createObjectBuilder()
									.add("tags", "Nil")
									.build();

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respObj.toString());
		}

		JsonObject respObj = Json.createObjectBuilder()
								.add("news", newsList.toString())
								.build();

		return ResponseEntity.ok(respObj.toString());

	}

}
