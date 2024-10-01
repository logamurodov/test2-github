package uz.pdp.bot;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pengrad.telegrambot.TelegramBot;

import com.pengrad.telegrambot.model.Location;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;


public class BotService {


    public static TelegramBot telegramBot = new TelegramBot("7451164902:AAGS16sBBAZmJ-8ZJgWuEy_DKFalfvAyw_E");

    public static TgUser getOrGreatUser(Long chatId) {
        for (TgUser tgUser : DB.TG_USERS) {
            if (tgUser.getChatId().equals(chatId)) {
                return tgUser;
            }
        }
        TgUser tgUser = new TgUser();
        tgUser.setChatId(chatId);
        DB.TG_USERS.add(tgUser);
        return tgUser;
    }

    @SneakyThrows
    public static void acceptStartAndUsers(TgUser tgUser) {
        SendMessage sendMessage=new SendMessage(
                tgUser.getChatId(),
                "Userlar ro'yhati"
        );
        HttpClient httpClient= HttpClient.newHttpClient();
        HttpRequest request=HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                .GET()
                .build();
        HttpResponse<String> respons = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String body = respons.body();
        Gson gson=new Gson();
        List<User> users= gson.fromJson(body, new TypeToken<List<User>>() {}.getType());

        InlineKeyboardMarkup inlineKeyboardMarkup=new InlineKeyboardMarkup();
        for (User user : users) {
            System.out.println(user.getName());
            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(user.getName()).callbackData("1"),new InlineKeyboardButton("posts").callbackData(String.valueOf(user.getId())));
        }
        sendMessage.replyMarkup(inlineKeyboardMarkup);
        tgUser.setTgState(TgState.USERS);
        telegramBot.execute(sendMessage);

    }

    @SneakyThrows
    public static void acceptUSersandPosts(TgUser tgUser, Integer messageId, String data) {
        DeleteMessage deleteMessage=new DeleteMessage(
                tgUser.getChatId(),
                messageId
        );
        SendMessage sendMessage=new SendMessage(
                tgUser.getChatId(),
                "Postlar"
        );
        HttpClient httpClient= HttpClient.newHttpClient();
        InlineKeyboardMarkup inlineKeyboardMarkup=new InlineKeyboardMarkup();
        HttpRequest request=HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                .GET()
                .build();
        HttpResponse<String> respons = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String body = respons.body();
        Gson gson=new Gson();
        List<Post> posts= gson.fromJson(body, new TypeToken<List<Post>>() {}.getType());
        List<Post> list = posts.stream().filter(post -> Integer.valueOf(data).equals(post.getUserId())).toList();
        for (Post post : list) {
            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(post.getTitle()).callbackData("Q"),new InlineKeyboardButton("comment").callbackData(String.valueOf(post.getId())));
        }
        sendMessage.replyMarkup(inlineKeyboardMarkup);
        tgUser.setTgState(TgState.POSTS);
        telegramBot.execute(deleteMessage);
        telegramBot.execute(sendMessage);
    }

    @SneakyThrows
    public static void acceptPostsAndComments(TgUser tgUser, Integer messageId, String data) {

        DeleteMessage deleteMessage=new DeleteMessage(
                tgUser.getChatId(),
                messageId
        );
        SendMessage sendMessage=new SendMessage(
                tgUser.getChatId(),
                "Commentlar"
        );
        HttpClient httpClient= HttpClient.newHttpClient();
        InlineKeyboardMarkup inlineKeyboardMarkup=new InlineKeyboardMarkup();
        HttpRequest request=HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/comments"))
                .GET()
                .build();
        HttpResponse<String> respons = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String body = respons.body();
        Gson gson=new Gson();
        List<Comment> comments= gson.fromJson(body, new TypeToken<List<Comment>>() {}.getType());
        List<Comment> list = comments.stream().filter(comment -> comment.getPostId().equals(Integer.valueOf(data))).toList();
        for (Comment comment : list) {
            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(comment.getName()).callbackData("W"));
        }
        sendMessage.replyMarkup(inlineKeyboardMarkup);
        tgUser.setTgState(TgState.POSTS);
        telegramBot.execute(deleteMessage);
        telegramBot.execute(sendMessage);

    }
}

