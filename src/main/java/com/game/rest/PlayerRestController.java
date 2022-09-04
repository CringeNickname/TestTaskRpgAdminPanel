package com.game.rest;

import com.game.entity.Player;
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
    public ResponseEntity<Integer> countPlayers() {
        List<Player> players = this.playerService.findAll();
        return new ResponseEntity<>(players.size(), HttpStatus.OK);
    }

    @RequestMapping(value = "id", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> getPlayer(@RequestParam("id") Long playerId) {
        if (playerId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Player player = this.playerService.findById(playerId);

        if (player == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Player>(player, HttpStatus.OK);
    }

    @RequestMapping(params = { "pageNumber", "pageSize", "order" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Player>> showPlayers(@RequestParam("pageNumber") Integer pageNumber, @RequestParam("pageSize") Integer pageSize, @RequestParam("order") String order) {
        List<Player> players = this.playerService.findAll();
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
        List<Player> returningListOfPlayers = players.subList(pageNumber * pageSize, (pageNumber * pageSize) + pageSize);
        return new ResponseEntity<>(returningListOfPlayers, HttpStatus.OK);

    }

}
