import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;

public class WebServer {

  public static void main(String[] args) throws IOException {
      int port = 8000;
      HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
      HttpContext context = server.createContext("/");
      context.setHandler(WebServer::handleRequest);
      server.start();
      System.out.printf("Started server listener on port  %s ... %n", port);
  }

  private static void handleRequest(HttpExchange exchange) throws IOException {

      String name = "NAME";
      String response = "Hello, " + name;
      exchange.sendResponseHeaders(200, response.getBytes().length);
      OutputStream os = exchange.getResponseBody();
      os.write(response.getBytes());
      os.close();
  }
}
