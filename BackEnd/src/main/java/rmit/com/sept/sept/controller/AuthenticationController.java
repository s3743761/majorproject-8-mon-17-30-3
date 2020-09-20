package rmit.com.sept.sept.controller;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;
import rmit.com.sept.sept.Booking;
import rmit.com.sept.sept.Company;
import rmit.com.sept.sept.CustomLoginSuccessHandler;
import rmit.com.sept.sept.Role;
import rmit.com.sept.sept.User;
import rmit.com.sept.sept.Worker;
import rmit.com.sept.sept.repository.BookingRepository;
import rmit.com.sept.sept.repository.RoleRepository;
import rmit.com.sept.sept.repository.UserRepository;
import rmit.com.sept.sept.service.BookingService;
import rmit.com.sept.sept.service.UserService;
import rmit.com.sept.sept.service.WorkerService;

import javax.validation.Valid;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson; 
import com.google.gson.GsonBuilder; 

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class AuthenticationController {


	

	// @Autowired
    // JdbcTemplate template;
    
    // @Autowired
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder encoder;

    private final UserService userService;
    
    private final BookingRepository bookingRepository;
    
    private int userID;

    private final CustomLoginSuccessHandler custom;
	// UserRepository userRepository;
	
	// @Autowired
	// BookingService bookingService;
	
	// @Autowired
	// WorkerService workerService;
	
    // public int userId;
    
    AuthenticationController(UserRepository userRepository,RoleRepository roleRepository,BCryptPasswordEncoder encoder,UserService userService,CustomLoginSuccessHandler custom, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.userService = userService;
        this.custom = custom;
        this.bookingRepository= bookingRepository;
    }
	
    @PostMapping("/loginUser")
	public String login(@RequestBody User newUser) {
    	System.out.println(newUser.getEmail());
        int id = userService.findIdLogin(newUser.getEmail());
        userID = id;
        String userType = userService.findUserType(id);
        System.out.println(userType);
    	System.out.println("here");
   
    	String jsonString = "{\"email\":\""+newUser.getEmail()+"\", \"userType\":\""+userType+"\"}"; 
        
    	
        return jsonString;
    
    }
    
	@GetMapping("/profile")
    public String profile(){
		userService.getUserDetails(userID);
		List<User> userList = userService.getList();

		String jsonString = "{\"email\":\""+userList.get(0).getEmail()+"\","
				+ " \"firstName\":\""+userList.get(0).getName()+ "\","
					+ " \"lastName\":\""+userList.get(0).getLastName() +"\"}"; 
        
    	
        return jsonString;
    }

	@PostMapping("/createUser")
	public User registerUser(@RequestBody User newUser) {
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        newUser.setStatus("VERIFIED");
        System.out.println(newUser.getCompanyName());
		
		if(newUser.getCompanyName() == null) {
			Role userRole = roleRepository.findByRole("SITE_USER");
			newUser.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		}
		else {
			newUser.setIsCompany(true);
			Role userRole = roleRepository.findByRole("ADMIN_USER");
			newUser.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		}
        return userRepository.save(newUser);
     
	}
	
	@PostMapping("/createBooking")
	public Booking createBooking(@RequestBody Booking newBooking) {
		newBooking.setUserId(userID);
		return bookingRepository.save(newBooking);
//        newUser.setPassword(encoder.encode(newUser.getPassword()));
//        newUser.setStatus("VERIFIED");
//        System.out.println(newUser.getCompanyName());
//		
//		if(newUser.getCompanyName() == null) {
//			Role userRole = roleRepository.findByRole("SITE_USER");
//			newUser.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
//		}
//		else {
//			newUser.setIsCompany(true);
//			Role userRole = roleRepository.findByRole("ADMIN_USER");
//			newUser.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
//		}
//        return userRepository.save(newUser);
     
	}
    
    /*
	@RequestMapping(value="/registerWorker", method=RequestMethod.POST)
	public String registerWorker2(@Valid Worker worker, BindingResult bindingResult, ModelMap modelMap) {
// 		ModelAndView modelAndView = new ModelAndView();
// 		// Check for the validations
// 		if(bindingResult.hasErrors()) {
// 			modelAndView.addObject("successMessage", "Please correct the errors in form!");
// 			modelMap.addAttribute("bindingResult", bindingResult);
// 		}
		
		
// //		else if(userService.isUserPresent(user)){
// //			modelAndView.addObject("successMessage", "user already exists!");			
// //		}
// //		// we will save the user if, no binding errors
// //		else {
// //			userService.saveUser(user);
// //			modelAndView.addObject("successMessage", "User is registered successfully!");
// //		}
// //		modelAndView.addObject("user", new User());
		
// 		modelAndView.setViewName("login");
// 		return modelAndView;
        return "{\"success\":1}";
	}
	
	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login"); // resources/template/login.html
		return modelAndView;
	}
	
	@RequestMapping(value="/createBooking", method=RequestMethod.POST)
	public ModelAndView createBooking(@Valid Booking booking) {
		ModelAndView modelAndView = new ModelAndView();
//		// Check for the validations
//		if(bindingResult.hasErrors()) {
//			modelAndView.addObject("successMessage", "Please correct the errors in form!");
//			modelMap.addAttribute("bindingResult", bindingResult);
//		}
//		
		// we will save the user if, no binding errors
//		else {
		
			booking.setUserId(userId);
			bookingService.createBooking(booking);
			modelAndView.addObject("successMessage", "Booking successfully!");
//		}
		modelAndView.addObject("booking", new Booking());
		modelAndView.setViewName("home");
		return modelAndView;
	}
	
	@RequestMapping(value = "company", method = RequestMethod.GET)
    public String company(@RequestParam (value = "search", required = false) String service_name, Model md){
        md.addAttribute("search", bookingService.findAllCompany(service_name));
        return "company";
    }
	
	@RequestMapping(value = "/bookings", method = RequestMethod.GET)
    public String booking(Model md){
        md.addAttribute("booking", bookingService.getAllBookings());

        return "bookings";
    }
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Model md){
		System.out.println("profile" + userId);
        md.addAttribute("user", userService.getUserDetails(userId));

        return "profile";
    }
	
	
	@RequestMapping(value = "/booking/{id}", method = RequestMethod.DELETE)
    public String deleteBooking(@PathVariable int id){
        bookingService.deleteBooking(id);
        return "bookings";
        
    }

	@RequestMapping(value = "/user{id}", method=RequestMethod.GET)
	@ResponseBody
	public User getUser(@PathVariable Integer id){

		if(userService.isUserPresent(id)){
			Optional<User> optional = userService.getUserRepository().findById(id);
			User myUser = optional.get();
			//userMap.put(id, userResponse);
			return myUser;
		}
		else{
			throw new RuntimeException("User doesn't exists");
		}
	}

	@RequestMapping(value = "/booking{id}", method=RequestMethod.GET)
	@ResponseBody
	public Booking getBooking(@PathVariable Integer id){

		if(bookingService.isBookingPresent(id)){
			Optional<Booking> optional = bookingService.getBookingRepository().findById(id);
			Booking myBooking = optional.get();
			//userMap.put(id, userResponse);
			return myBooking;
		}
		else{
			throw new RuntimeException("Booking doesn't exists");
		}
    }
    */
	
	

}

