package org.eightyoctane.rabbitmessage;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.eightyoctane.rabbitmessage.Message;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired AmqpTemplate amqpTemplate;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/")
	public String home(Model model) {
				
		model.addAttribute(new Message());
		
		return "WEB-INF/views/home.jsp";
	}
	
	@RequestMapping(value = "/publish", method=RequestMethod.POST)
	public String publish(Model model, Message message) {
		// Send a message to the "messages" queue
		amqpTemplate.convertAndSend("messages", message.getValue());
		model.addAttribute("published", true);
		return home(model);
	}
	
	@RequestMapping(value = "/get", method=RequestMethod.POST)
	public String get(Model model) {
		String message = (String)amqpTemplate.receiveAndConvert("messages");
		if (message != null)
			model.addAttribute("got", message);
		else
			model.addAttribute("got_queue_empty", true);
		
		return home(model);
	}
	
	
}
