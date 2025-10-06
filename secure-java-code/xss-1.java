import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

PolicyFactory policy = new HtmlPolicyBuilder()

.allowElements("a")
.allowUrlProtocols("https")
.allowAttributes("href").onElements("a")
.toFactory();

String userInput = request.getParameter("input");
String output = policy.sanitize(userInput);
document.getElementById("output").innerHTML = "You entered: " + output;