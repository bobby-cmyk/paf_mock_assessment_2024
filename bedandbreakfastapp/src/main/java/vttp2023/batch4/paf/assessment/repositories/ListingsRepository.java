package vttp2023.batch4.paf.assessment.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.assessment.Utils;
import vttp2023.batch4.paf.assessment.models.Accommodation;
import vttp2023.batch4.paf.assessment.models.AccommodationSummary;

@Repository
public class ListingsRepository {
	
	// You may add additional dependency injections

	@Autowired
	private MongoTemplate template;

	/*

	 * db.listings.distinct(
		'address.suburb', 
		{
		'address.country': {$regex : "Australia", $options : "i"},
		$nor : [{'address.suburb' : null}, {'address.suburb' : ''}]
		}
)
	 *
	 */
	public List<String> getSuburbs(String country) {
		
		Query query = new Query();

		query.addCriteria(
			Criteria.where("address.country").regex(country, "i")
				.norOperator(
					Criteria.where("address.suburb").is(null), 
					Criteria.where("address.suburb").is(""))
				);
		
		/*
		 * Query query = Query.query(
				Criteria.where("address.country").regex(country, "i")
					.norOperator(
						Criteria.where("address.suburb").is(null), 
						Criteria.where("address.suburb").is("")
					);
		 * 
		 */
		
		List<String> suburbs = template.findDistinct(query, "address.suburb", "listings", String.class);

		return suburbs;
	}

	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * eg. db.bffs.find({ name: 'fred }) 
	 *	
	 
	 
	db.listings.find({
		$and: [
			{'address.suburb' : {$regex : 'monterey', $options : 'i'}},
			{price: {$lte: 500}},
			{accommodates: {$gte: 2}},
			{min_nights: {$lte: 5}}
		]
	})
	.projection({
		_id: 1,
		name : 1,
		accommodates : 1,
		price: 1
	})
	.sort({
		price: -1
	})


	 *
	 */
	public List<AccommodationSummary> findListings(String suburb, int persons, int duration, float priceRange) {

		Query query = new Query();

		Criteria criteria = new Criteria();

		criteria.andOperator(
			Criteria.where("address.suburb").regex(suburb, "i"),
			Criteria.where("accommodates").gte(persons),
			Criteria.where("min_nights").lte(duration),
			Criteria.where("price").lte(priceRange)
		);

		query.addCriteria(criteria);

		// Projection
		query.fields()
			.include("_id", "name", "accommodates", "price");

		// Sort
		query.with(Sort.by(Sort.Direction.DESC, "price"));

		List<Document> docs = template.find(query, Document.class, "listings");

		List<AccommodationSummary> accomSums = new ArrayList<>();

		for (Document doc : docs) {
			AccommodationSummary accomSum = new AccommodationSummary();
			accomSum.setId(doc.getString("_id"));
			accomSum.setName(doc.getString("name"));
			accomSum.setAccomodates(doc.getInteger("accommodates"));
			accomSum.setPrice(doc.get("price", Number.class).floatValue());

			accomSums.add(accomSum);
		}

		// System.out.printf(">>> AccomsSum: %d\n", accomSums.size());
	
		return accomSums;
	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	public Optional<Accommodation> findAccommodatationById(String id) {
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = Query.query(criteria);

		List<Document> result = template.find(query, Document.class, "listings");
		if (result.size() <= 0)
			return Optional.empty();

		return Optional.of(Utils.toAccommodation(result.getFirst()));
	}

}
