package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.ActionRow;


import javax.security.auth.login.LoginException;
import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.Map;

public class Main extends ListenerAdapter
{
    private static final String OwnerID = "484788394547740672";
    private static Dotenv dot;
    private static final Map<String, String> stripeLinks = new HashMap<>();
    static {
        stripeLinks.put("5 ISPS", "https://buy.stripe.com/3cseYPctJ5jvfMQ8xd");
        stripeLinks.put("10 ISPS", "https://buy.stripe.com/dR6eYPdxNdQ10RWbJq");
        stripeLinks.put("20 ISPS", "https://buy.stripe.com/9AQ8ArdxN5jv8koeVD");
        stripeLinks.put("25 ISPS", "https://buy.stripe.com/00gg2T65ldQ13047tc");
        stripeLinks.put("50 ISPS", "https://buy.stripe.com/4gwdUL65l4frgQU7td");
        stripeLinks.put("75 ISPS", "https://buy.stripe.com/eVa4kbgJZ13f0RWdRC");
        stripeLinks.put("100 ISPS", "https://buy.stripe.com/dR6aIzgJZeU5asw14R");
        stripeLinks.put("150 ISPS", "https://buy.stripe.com/5kAcQH79pdQ10RWdRE");
        stripeLinks.put("200 ISPS", "https://buy.stripe.com/fZe8AralB7rD8ko00P");
        stripeLinks.put("254 ISPS", "https://buy.stripe.com/3cseYP1P59zL8ko8xm");
        stripeLinks.put("Residentials", "https://buy.stripe.com/aEUaIz2T98vHbwAaFw");
    }

    public static void main(String[] args) throws LoginException, InterruptedException
{
    dot = Dotenv.load();

    String token = dot.get("DISCORD_BOT_TOKEN");

    var bot = JDABuilder.createDefault(token)
            .setActivity(Activity.playing("Referral Bot"))
            .addEventListeners(new Main())
            .build();

    bot.awaitReady();

    bot.getGuildById("872201972420333628").upsertCommand("spawnrefpanel", "Spawn the referral selection panel").queue();
}

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        if (!"spawnrefpanel".equals(event.getName())) return;

        if (!OwnerID.equals(event.getUser().getId()))
        {
            event.reply("You are not allowed to spawn this channel").setEphemeral(true).queue();
            return;
        }

        var embed = new net.dv8tion.jda.api.EmbedBuilder()
                .setTitle("Volt Referral Panel")
                .setDescription("Click the button below to generate your referral link.\n\n" +
                        "Choose how many ISPs you’d like, and a unique link will be generated")
                .setColor(new java.awt.Color(188,0,1,255))
                .build();

        var button = net.dv8tion.jda.api.interactions.components.buttons.Button.primary("open_dropdown", "Select Product");

        event.replyEmbeds(embed)
                .addActionRow(button)
                .setEphemeral(false)
                .queue();
    }

    @Override
    public void onButtonInteraction(net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent event) {
        if (!"open_dropdown".equals(event.getComponentId())) return;

        StringSelectMenu menu = StringSelectMenu.create("product_select")
                .setPlaceholder("Choose a product...")
                .addOption("5 ISPS", "5 ISPS")
                .addOption("10 ISPS", "10 ISPS")
                .addOption("20 ISPS", "20 ISPS")
                .addOption("25 ISPS", "25 ISPS")
                .addOption("50 ISPS", "50 ISPS")
                .addOption("75 ISPS", "75 ISPS")
                .addOption("100 ISPS", "100 ISPS")
                .addOption("150 ISPS", "150 ISPS")
                .addOption("200 ISPS", "200 ISPS")
                .addOption("254 ISPS", "254 ISPS")
                .addOption("Residentials", "Residentials")
                .build();

        event.reply("Select a product below:")
                .addActionRow(menu)
                .setEphemeral(true)
                .queue();
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (!"product_select".equals(event.getComponentId())) return;

        String selectedKey = event.getValues().get(0);
        String baseLink = stripeLinks.get(selectedKey);

        if (baseLink == null)
        {
            event.reply("Invalid product selected").setEphemeral(true).queue();
            return;
        }

        String referralLink = baseLink + "?client_reference_id=" + event.getUser().getId();

        event.reply("Here is your referral link: " + referralLink)
                .setEphemeral(true)
                .queue();
    }


}