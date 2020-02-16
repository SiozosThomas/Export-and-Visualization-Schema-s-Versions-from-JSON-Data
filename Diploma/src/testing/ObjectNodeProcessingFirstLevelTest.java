package testing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.util.Pair;
import junit.framework.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import data_processing.ObjectNode;
import data_processing.ObjectNodeProcessing;

@SuppressWarnings("deprecation")
class ObjectNodeProcessingFirstLevelTest {

	private JsonFactory factory;
	private JsonParser parser;
	private JsonNode jsonNode;
	private ObjectNodeProcessing objectNodeProcessing;
	
	@BeforeEach
	public void setUp() {
		factory = new JsonFactory();
		parser = null;
		jsonNode = null;
		objectNodeProcessing = new ObjectNodeProcessing();
	}
	
	@Test
	void runTests() {
		String file = "testing_files/object_node_processing_tests/"
				+ "first_level_test.json";
		objectNodeProcessing.setObjectNode(openJsonFile(file));
		testAllFields();
		testPrimitives();
		testObjects();
	}
	
	private void testAllFields() {
		ArrayList<Pair<String, String>> allFields = fillAllFields();
		Assert.assertEquals(allFields,
				objectNodeProcessing.processObject("no_root").getAllFields());
	}
	
	private void testPrimitives() {
		HashMap<String, String> primitives = fillPrimitives();
		Assert.assertEquals(primitives,
				objectNodeProcessing.processObject("no_root").getPrimitives());
	}
	
	private void testObjects() {
		HashMap<String, ObjectNode> objects = fillObjects();
		Assert.assertEquals(objects.get("programming_languages").getAllFields(),
				objectNodeProcessing.processObject("no_root").
				getObjects().get("programming_languages").getAllFields());
		Assert.assertEquals(objects.get("programming_languages").getPrimitives(),
				objectNodeProcessing.processObject("no_root").
				getObjects().get("programming_languages").getPrimitives());
		
	}
	
	private ArrayList<Pair<String, String>> fillAllFields() {
		ArrayList<Pair<String, String>> allFields =
				new ArrayList<Pair<String, String>>();
		allFields.add(new Pair<>("id", "IntNode"));
		allFields.add(new Pair<>("first_name", "TextNode"));
		allFields.add(new Pair<>("last_name", "TextNode"));
		allFields.add(new Pair<>("nickname", "TextNode"));
		allFields.add(new Pair<>("age", "IntNode"));
		allFields.add(new Pair<>("programming_languages", "ObjectNode"));
		return allFields;
	}
	
	private HashMap<String, String> fillPrimitives() {
		HashMap<String, String> primitives = new HashMap<String, String>();
		primitives.put("id", "IntNode");
		primitives.put("first_name", "TextNode");
		primitives.put("last_name", "TextNode");
		primitives.put("nickname", "TextNode");
		primitives.put("age", "IntNode");
		return primitives;
	}
	
	private HashMap<String, ObjectNode> fillObjects() {
		HashMap<String, ObjectNode> objects =
				new HashMap<String, ObjectNode>();
		ObjectNode objectNode = new ObjectNode();
		objectNode.addPrimitive("java", "IntNode");
		objectNode.addPrimitive("python", "IntNode");
		objectNode.addField("java", "IntNode");
		objectNode.addField("python", "IntNode");
		objects.put("programming_languages", objectNode);
		return objects;
	}
	
	private JsonNode openJsonFile(String file) {
		try {
			parser = factory.createParser(new File(file));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		parser.setCodec(new ObjectMapper());
		try {
			jsonNode = parser.readValueAsTree();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonNode;
	}
}