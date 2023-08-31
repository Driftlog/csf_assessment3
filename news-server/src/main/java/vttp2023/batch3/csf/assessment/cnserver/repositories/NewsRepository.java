package vttp2023.batch3.csf.assessment.cnserver.repositories;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class NewsRepository {

	@Autowired
	private MongoTemplate template;
	// TODO: Task 1 
	// Write the native Mongo query in the comment above the method
	
//db.news.insert(news)
    public Document insertNews(Document news) {
        Document newReview = template.insert(news, "news");
       
        newReview.get("_id");
        return newReview;
    }

	//MongoQuery
	//Unwind, Check if PostDate > 5mins, Sort by Tag, project tag and count, limit 10
	//db.news.aggregate([
		//{$match: {postDate: {$gte: minutes*60000}},  {tags: {"$exists: true", "$ne": ""}}},
	//{$unwind: "$tags"}
	//	{$group: {$_id : "$tags"}},
	//{$limit {
			//10
		//}}
	// {$project: {
			// tags : 1,
			//  count: 1
		//}
	//}
	//])
	// TODO: Task 2 
	// Write the native Mongo query in the comment above the method
	public List<Document> getNewsByTime(int minutes) {

		long time = minutes * 60000;

		UnwindOperation unwindTags = Aggregation.unwind("tag");
		MatchOperation matchTime = Aggregation.match(Criteria.where("postDate").lte(time)
								.andOperator(Criteria.where("tag").ne(""))
								.andOperator(Criteria.where("tag").exists(true)));
											
		GroupOperation groupByTag = Aggregation.group("tag")
											.count().as("count");
		SortOperation sortOperation = Aggregation.sort(Sort.by(Direction.DESC, "count"));

		ProjectionOperation projectOperation = Aggregation.project("tag", "count");

		LimitOperation limitOperation = Aggregation.limit(10);
		
		Aggregation pipeline = Aggregation.newAggregation( matchTime, unwindTags, groupByTag, sortOperation, limitOperation, projectOperation );
	
		AggregationResults<Document> results = template.aggregate(pipeline, "news", Document.class);

		return results.getMappedResults();

	}


	// TODO: Task 3
	// db.news.aggregate([{$unwind: "$tags"}, {$match: {postDate: {$gte: minutes*60000}}, {tags: tag}}}])
	// Write the native Mongo query in the comment above the method
	public List<Document> getNewsByTag(int minutes, String tag) {

		long time = minutes * 60000;

		AggregationOperation unwindTags = Aggregation.unwind("tag");
		
		MatchOperation matchTime = Aggregation.match(Criteria.where("postDate").lte(time)
										.andOperator(Criteria.where("tag").is(tag)));

		Aggregation pipeline = Aggregation.newAggregation(unwindTags, matchTime);

		AggregationResults<Document> results = template.aggregate(pipeline, "news", Document.class);

		return results.getMappedResults();
	}


}
