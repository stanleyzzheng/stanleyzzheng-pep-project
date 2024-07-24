package Controller;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints
 * you will need can be found in readme.md as well as the test cases. You should refer to prior
 * mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI()
     * method, as the test suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService accountService;
    MessageService messageService;

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register", this::registerAccountHandler);
        app.post("login", this::loginHandler);
        app.post("messages", this::createMessageHandler);
        app.get("messages", this::retrieveAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByMessageIdHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("messages/{message_id}", this::updateMessageTextHandler);
        app.get("accounts/{account_id}/messages",this::retrieveAllMessagesByUserHandler);

        return app;
    }

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    public void retrieveAllMessagesByUserHandler(Context ctx) throws JsonProcessingException{
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessageByUser(account_id);
        ctx.json(messages);
    }
    private void retrieveAllMessagesHandler(Context ctx) throws JsonProcessingException{
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }
    private void getMessageByMessageIdHandler(Context ctx) throws JsonProcessingException{
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        if(message == null) ctx.status(200);
        else ctx.json(message);

    }
    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException{
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message messageDeleted = messageService.deleteMessageById(message_id);
        if(messageDeleted != null) ctx.json(messageDeleted).status(200);
        else ctx.status(200);
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.createMessage(message);
        if (newMessage != null){
            ctx.json(om.writeValueAsString(newMessage));
        }else{
            ctx.status(400);
        }
    }

    private void updateMessageTextHandler(Context ctx) throws JsonProcessingException{
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> updates = om.readValue(ctx.body(), Map.class);
        String updateMessageText = (String) updates.get("message_text");
        Message updatedMessage = messageService.updateMessageById(message_id, updateMessageText);
        if(updatedMessage != null)ctx.json(updatedMessage).status(200);
        else ctx.status(400);
        
    }
    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account loginAccount = accountService.loginAccount(account);
        if(loginAccount != null){
            ctx.json(om.writeValueAsString(loginAccount));
        }else{
            ctx.status(401);
        }
    }

    private void registerAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.registerAccount(account);
        // System.out.println(addedAccount);
        if (addedAccount != null) {
            ctx.json(om.writeValueAsString(addedAccount));
        } else {
            ctx.status(400);
        }
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and
     *        response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}
