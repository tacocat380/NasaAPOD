package src;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

class call{
    public static void main(String[]args){

        String call = "https://api.nasa.gov/planetary/apod?api_key=Pd1xaYMIuh4V57laVLg9KZUv3sUc9by2cohwJ8hg";
        URI Myuri = createMyURI(call);
        System.out.println(Myuri.toString());

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder(Myuri).build();        // sends inital request
        HttpResponse<String> response = sendResponseS(client, request);
        System.out.println("Response: " + response.body());

        int start = response.body().indexOf("https://apod.nasa.gov/apod/image/");    // parses out the pictureurl for return for image body
        int end = response.body().indexOf(".jpg",start);
        String pictureUrl = response.body().substring(start, end+4);

        start = response.body().indexOf("title");   // parses out the title from the response body
        end = response.body().indexOf(",",start);
        String title = response.body().substring(start + 8, end-1);
        System.out.println("Title: " + title);

        int urlType = response.body().indexOf("media_type\":\"image"); // ensures the returned url type is correct, we only handle images for now
        if(urlType == -1){                                  
            System.out.println("Invalid url type.");
            System.exit(-1);
        }

        BufferedImage image = imageRequest(pictureUrl);         // creates and sends request using the url returned from the initial response for the image data 
        String imagePath = imageStore(image,title);                                // stores image in image folder 
        wallpaperSwap(imagePath);                                    // swaps wallpaper 
        System.out.println("Done");
    }


    static {
        String path = System.getProperty("user.dir") + "\\libaries\\swap.dll"; 
        try{
        System.load(path);             // loads swap.exe 
        }
        catch(java.lang.UnsatisfiedLinkError err){
            System.out.println("Error with JNI library");
            System.exit(0);
        }
    }

    public static native void wallpaperSwap(String path); // interface 
   
    private static String validFileName(String name){
        name = name.replaceAll("\\s", "");
        name = name.replaceAll(":", "");
        name = name.replaceAll("!", "");
        return name;
    }

    private static String imageStore(BufferedImage image, String title){
        title = validFileName(title);
        String location = System.getProperty("user.dir") + System.getProperty("file.separator") + "images" + System.getProperty("file.separator") + title + ".jpg";
        try{
            File outputfile = new File(location);
            ImageIO.write(image,"jpg",outputfile);
        }
        catch (IOException e){
            System.out.println("store had an issue: " + e.getMessage());
        }
        System.out.println("Image saved to: " + location);
        return System.getProperty("user.dir") + System.getProperty("file.separator") + "images" + System.getProperty("file.separator") + title + ".jpg";
    }

    private static BufferedImage imageRequest(String pictureUrl){
    URL url_picture = null;
    BufferedImage image = null;
    try{
         url_picture = new URL(pictureUrl);
         image = ImageIO.read(url_picture);
    }
    catch (MalformedURLException e){
        System.out.println("imageRequest had an issue: " + e.getMessage());
    }
    catch (IOException e){
        System.out.println("imageRequest had an issue: " + e.getMessage());
    }
    return image;
    }

    private static URI createMyURI(String path){
    URI u = null;
    try{
        u = new URI(path);
    }
    catch (URISyntaxException e){
        System.out.println("createMyURI had an issue: " + e.getMessage());
    }
    return u;
    }

    private static HttpResponse<String> sendResponseS(HttpClient client,HttpRequest inputRequest){
       HttpResponse<String> response = null;
        try {
            response = client.send(inputRequest, BodyHandlers.ofString());
        }
        catch (Exception e){;
            System.out.println("sendResponse had an issue: " + e.getMessage());
        }
        return response;
    }
}