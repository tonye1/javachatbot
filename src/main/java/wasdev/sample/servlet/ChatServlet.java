package wasdev.sample.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.InputData;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

/**
 * Servlet implementation class ChatServlet
 */
@WebServlet("/ChatServlet")
public class ChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChatServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Im here - posting");
		doPost(req,resp);
	}

	/**
	 * @throws IOException
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		out.println("<p>Yo</p>");
		String requestMessage = request.getParameter("message");
		String contextString = request.getParameter("context");
		JSONObject contextObject = new JSONObject();

		if (contextString != null) {
			contextObject = JSONObject.parseObject(contextString);
		}

		System.out.println("Context: ");
		System.out.println(contextObject);

		//Map<String, Object> contextMap = Utility.toMap(contextObject);

		if (requestMessage == null || requestMessage.isEmpty()) {
			requestMessage = "Greetings";
		}

		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");

		/*
		 * ConversationService service = new
		 * ConversationService(ConversationService.VERSION_DATE_2017_02_03);
		 * service
		 * .setUsernameAndPassword(Configuration.getInstance().CONVERSATION_USERNAME
		 * , Configuration.getInstance().CONVERSATION_PASSWORD);
		 * 
		 * MessageRequest newMessage = new
		 * MessageRequest.Builder().context(contextMap
		 * ).inputText(requestMessage).build();
		 * 
		 * try { MessageResponse r =
		 * service.message(Configuration.getInstance().
		 * CONVERSATION_WORKSPACE_ID, newMessage).execute();
		 * 
		 * response.getWriter().append(r.toString()); }
		 */
		Conversation service = new Conversation(
				Conversation.VERSION_DATE_2017_05_26);
		service.setUsernameAndPassword("", "");

		try {

			InputData input = new InputData.Builder(requestMessage).build();
			String workspaceId = "";
			MessageOptions options = new MessageOptions.Builder(workspaceId)
					.input(input).build();
			MessageResponse msgresponse = service.message(options).execute();
			System.out.println(msgresponse);
			out.println("<p>" + msgresponse + "</p>");
		} catch (Exception ex) {

			JSONObject r = new JSONObject();
			JSONObject output = new JSONObject();
			JSONArray text = new JSONArray();
			text.add(ex.getLocalizedMessage());
			output.put("text", text);
			r.put("output", output);
			response.getWriter().append(r.toString());
		}
	}
}
