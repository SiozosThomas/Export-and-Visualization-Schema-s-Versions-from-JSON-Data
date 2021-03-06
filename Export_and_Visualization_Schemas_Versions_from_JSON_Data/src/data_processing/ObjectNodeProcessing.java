package data_processing;

import java.util.Iterator;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;

public class ObjectNodeProcessing {
	
	private JsonNode node;
	private JsonNodeType jsonNodeType;
	private ObjectNode objectNode;
	
	public ObjectNodeProcessing() {
		node = null;
		jsonNodeType = new JsonNodeType();
		objectNode = new ObjectNode();
	}

	public void setObjectNode(JsonNode node) {
		this.node = node;
	}

	public ObjectNode getObjectNode() {
		return objectNode;
	}
	
	public void setId(int id) {
		objectNode.setId(id);
	}
	
	public ObjectNode processObject(String parent) {
		Iterator<Map.Entry<String, JsonNode>> objectIterator = node.fields();
		Map.Entry<String, JsonNode> nextField;
		objectNode.setObjectName(parent);
		while (objectIterator.hasNext()) {
			nextField = objectIterator.next();
			jsonNodeType.setJsonNode(nextField.getValue());
			if (jsonNodeType.getTypeAsString().equals("ObjectNode")) {
				objectNode.addField(nextField.getKey(),
						jsonNodeType.getTypeAsString());
				ObjectNodeProcessing object = new ObjectNodeProcessing();
				object.setObjectNode(nextField.getValue());
				object.processObject(parent + "/" + nextField.getKey());
				objectNode.addObject(nextField.getKey(),
						object.getObjectNode());
			} else {
				objectNode.addField(nextField.getKey(),
						jsonNodeType.getTypeAsString());
			}
		}
		return objectNode;
	}
}
