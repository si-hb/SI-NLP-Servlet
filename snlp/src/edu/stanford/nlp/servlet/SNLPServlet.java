package edu.stanford.nlp.servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

public class SNLPServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6526362765621762054L;
	
	 public void init(ServletConfig config) throws ServletException {
            super.init(config);
            
            try {
            	// Does something
            } catch(Exception es) {
            	es.printStackTrace();
            }
     }

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// Verify parameters
		boolean noParams = false;
		if (request.getParameter("command") == null || request.getParameter("sentence") == null) {
			noParams = true;
		}
		
		// Get parameters
		String command = request.getParameter("command");
		if (command == null || command == "") command = "-retainTMPSubcategories -outputFormat wordsAndTags,penn,typedDependencies edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz -";

		String sentence = request.getParameter("sentence");
		if (sentence == null || sentence == "") sentence = "I look forward to hearing from you.";

		// Return parsing result
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Stanford Native Language Parser";
		
		out.println("<html>");
		out.println("<title>" + title + "</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h3>" + title + "</h3>");
		out.println("<form name='sentence' action='/snlp/snlprun' method='get'>");
		out.println("<br />" + request.getRealPath("WEB-INF\\classes").replace('\\', '/') + "/<br />");
		out.println("<table>");
		out.println("	<tr>");
		out.println("		<td>");
		out.println("			<label for='command'>Command : </label>");
		out.println("		</td>");
		out.println("		<td>");
		out.println("			<input type='text' name='command' value='' size='100' maxlength='150' />");
		out.println("		</td>");
		out.println("	</tr>");
		out.println("	<tr>");
		out.println("		<td>");
		out.println("		</td>");
		out.println("		<td>");
		out.println(command);
		out.println("		</td>");
		out.println("	</tr>");
		out.println("	<tr>");
		out.println("		<td>");
		out.println("			<label for='sentence'>Sentence : </label>");
		out.println("		</td>");
		out.println("		<td>");
		out.println("			<input type='text' name='sentence' value='' size='120' maxlength='200' />");
		out.println("		</td>");
		out.println("	</tr>");
		out.println("	<tr>");
		out.println("		<td>");
		out.println("		</td>");
		out.println("		<td>");
		out.println(sentence);
		out.println("		</td>");
		out.println("	</tr>");
		out.println("	<tr>");
		out.println("		<td>");
		out.println("		</td>");
		out.println("		<td>");
		out.println("			<input type='Submit' name='submit' value='Submit' />");
		out.println("		</td>");
		out.println("	</tr>");
		out.println("</table>");
		out.println("</form>");
		out.println("<br />");
		
		out.println("<h4>Result : </h4>");
		out.println("<pre>");
		
		long start = System.currentTimeMillis();

		// Run Lexical Parser
		String root_path = request.getRealPath("WEB-INF\\classes").replace('\\', '/') + "/";
		String[] args = command.split(" ");
		LexicalizedParser.web_main(args, sentence, out, root_path);
		
		// End response
		out.println("</pre>");
		
		long elapsedTimeMillis = System.currentTimeMillis() - start;
		out.println("<br />Elapsed Time(ms) : " + Long.toString(elapsedTimeMillis));
		
		out.println("</body>");
		out.println("</html>");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}
}
