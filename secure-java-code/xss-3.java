// Phương pháp Fix 1: Sử dụng HTML Encoding
import org.apache.commons.text.StringEscapeUtils; 
// Cần thêm dependency cho Apache Commons Text

@RestController
public class XSSControllerFixed {

    @GetMapping("/hello")
    ResponseEntity<String> helloSafe(@RequestParam(value = "name", defaultValue = "World") String name) {
        
        // MÃ FIX: Mã hóa chuỗi đầu vào (HTML Encoding)
        String safeName = StringEscapeUtils.escapeHtml4(name);
        
        // Trả về chuỗi đã được mã hóa
        return new ResponseEntity<>("Hello World! " + safeName, HttpStatus.OK);
    }
}


// Phương pháp Fix 2: Sử dụng Thymeleaf (Được khuyến nghị cho các ứng dụng web)
// Controller chuyển sang trả về View (Model)
@Controller 
public class XSSControllerTemplate { 
    @GetMapping("/hello-view")
    public String helloView(@RequestParam(value = "name", defaultValue = "World") String name, Model model) {
        model.addAttribute("username", name);
        return "greeting-page"; // Trả về tên file HTML (greeting-page.html)
    }
}