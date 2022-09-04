package com.game.rest;

import com.game.entity.Player;
import com.game.entity.Race;
import com.game.rest.Comparators.BirthdayComparator;
import com.game.rest.Comparators.ExperienceComparator;
import com.game.rest.Comparators.IdComparator;
import com.game.rest.Comparators.NameComparator;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("rest/players")
public class PlayerRestController {


    private final PlayerService playerService;

    @Autowired
    public PlayerRestController(PlayerService playerService) {
        this.playerService = playerService;

    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Player>> showAllPlayers() {
        List<Player> players = this.playerService.findAll();
        players = players.subList(0, 3);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @RequestMapping(value = "count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> countPlayers(@RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "title", required = false) String title,
                                                @RequestParam(value = "before", required = false) String before,
                                                @RequestParam(value = "after", required = false) Long after,
                                                @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                                @RequestParam(value = "race", required = false) String race,
                                                @RequestParam(value = "profession", required = false) String profession,
                                                @RequestParam(value = "banned", required = false) Boolean banned) {

        List<Player> players = this.playerService.findAll();
        List<Player> filteredPlayers = new ArrayList<>();

        if (name != null) {
            for (Player player : players) {
                if (player.getName().contains(name)) {
                    filteredPlayers.add(player);
                }
            }
            players.retainAll(filteredPlayers);
            filteredPlayers = new ArrayList<>();
        }
        return new ResponseEntity<>(players.size(), HttpStatus.OK);
    }

    @RequestMapping(params = { "pageNumber", "pageSize", "order" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Player>> showPlayers(@RequestParam("pageNumber") Integer pageNumber, @RequestParam("pageSize") Integer pageSize, @RequestParam("order") String order,
                                                    @RequestParam(value = "name", required = false) String name,
                                                    @RequestParam(value = "title", required = false) String title,
                                                    @RequestParam(value = "before", required = false) String before,
                                                    @RequestParam(value = "after", required = false) Long after,
                                                    @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                    @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                    @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                    @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                                    @RequestParam(value = "race", required = false) String race,
                                                    @RequestParam(value = "profession", required = false) String profession,
                                                    @RequestParam(value = "banned", required = false) Boolean banned) {

        List<Player> players = this.playerService.findAll();
        List<Player> filteredPlayers = new ArrayList<>();

        switch (order) {
            case "NAME" :
                players.sort(new NameComparator());
                break;
            case "EXPERIENCE" :
                players.sort(new ExperienceComparator());
                break;
            case "BIRTHDAY" :
                players.sort(new BirthdayComparator());
                break;
            default: players.sort(new IdComparator());
        }

        if (name != null) {
//            pageNumber = 0;
            for (Player player : players) {
                if (player.getName().contains(name)) {
                    filteredPlayers.add(player);
                }
            }
            players.retainAll(filteredPlayers);
            filteredPlayers = new ArrayList<>();
        }

        try {
            List<Player> returningListOfPlayers = players.subList(pageNumber * pageSize, (pageNumber * pageSize) + pageSize);
            return new ResponseEntity<>(returningListOfPlayers, HttpStatus.OK);
        }
        catch (IndexOutOfBoundsException e) {
            try {
                return new ResponseEntity<>(players.subList(players.size() - pageSize, players.size()), HttpStatus.OK);
            }
            catch (IndexOutOfBoundsException e1) {
                return new ResponseEntity<>(players, HttpStatus.OK);
            }
        }

    }

}
