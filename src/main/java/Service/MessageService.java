package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message message) {
        if (message.message_text == "" || message.message_text.length() > 255
                || accountDAO.getAccountByAccountId(message.getPosted_by()) == null)
            return null;

        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.retrieveMessageById(messageId);
    }

    public Message deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }

    public Message updateMessageById(int messageId, String updatedMessageText) {
        if (updatedMessageText == "" || updatedMessageText.length() > 255
                || messageDAO.retrieveMessageById(messageId) == null)
            return null;
        return messageDAO.updateMessageById(messageId, updatedMessageText);
    }
    public List<Message> getAllMessageByUser(int account_id){
        return messageDAO.getAllMessagesByUser(account_id);
    }
}
