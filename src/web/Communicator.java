package web;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import sun.misc.IOUtils;
import utils.Vector2;

public class Communicator{	
	public enum ERequestType{
		GAME_START("GameStart"),
		UPDATE_PLAYERS("UpdatePlayers"),
		UPDATE_ROOMS("UpdateRooms"),
		ENTER_ROOM("EnterRoom"),
		LEAVE_ROOM("LeaveRoom"),
		MOVE("Move"),
		BOMB("Bomb"),
		EXPLODE("Explode"),
		DIE("Die"),
		GET_PID("GetPid"),
		GAME_END("GameEnd"),
		INVALID("");
		
		private final String action;
		
		private ERequestType(String a){
			action = a;
		}
		
		public String getAction(){
			return action;
		}
	}
	
	private static final String url = "http://localhost/php/communicator.php";
	private static final CloseableHttpClient client = HttpClientBuilder.create().build();
	private static final HttpPost request = new HttpPost(url);
	
	private Communicator(){}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		client.close();
	}
	
	public static final JSONObject sendRequest(ERequestType type){
		return sendRequest(type, 0, 0);
	}
	
	public static final JSONObject sendRequest(ERequestType type, int gameRoom, int pid){
		return sendRequest(type, gameRoom, pid, 0, Vector2.zero);
	}
	
	public static final JSONObject sendRequest(ERequestType type, int gameRoom, int pid, int id, Vector2 pos){
		JSONObject req = new JSONObject();
		req.put("action", type.getAction());
		switch (type){
			case BOMB:
				req.put("id", id);
				break;
			case DIE:
				break;
			case ENTER_ROOM:
				break;
			case EXPLODE:
				break;
			case GAME_END:
				break;
			case GAME_START:
				break;
			case GET_PID:
				break;
			case LEAVE_ROOM:
				break;
			case MOVE:
				break;
			case UPDATE_PLAYERS:
				break;
			case UPDATE_ROOMS:
				break;
			default:
				break;			
		}
		if (type != ERequestType.GET_PID && type != ERequestType.UPDATE_ROOMS){
			req.put("gameRoom", gameRoom);
			req.put("pid", pid);
			req.put("x", pos.x);
			req.put("y", pos.y);
		}
		return sendRequest(req);
	}
	
	public static final JSONObject sendRequest(JSONObject json){
		JSONObject resp = null;
		try {        
	        StringEntity params = new StringEntity(json.toString());
	        params.setContentType("application/json");
	        request.addHeader("Content-type", "application/json");
	        request.setEntity(params);
	        HttpResponse response = client.execute(request);
	        String line = new String(IOUtils.readFully(response.getEntity().getContent(), -1, true)).replace("\\", "");
	        line = line.substring(1, line.length() - 1);
	        System.out.println(line);
	        resp = new JSONObject(line);
	    }
		catch (Exception e) {
	        e.printStackTrace();
	        return resp;
	    }
		return resp;
	}
}
