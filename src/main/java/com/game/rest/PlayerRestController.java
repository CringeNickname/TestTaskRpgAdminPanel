package com.game.rest;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

import com.game.rest.comparators.BirthdayComparator;
import com.game.rest.comparators.ExperienceComparator;
import com.game.rest.comparators.IdComparator;
import com.game.rest.comparators.NameComparator;
import com.game.service.FilterService;
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

    private final FilterService filterService;
    private final PlayerService playerService;

    @Autowired
    public PlayerRestController(FilterService filterService, PlayerService playerService) {
        this.filterService = filterService;
        this.playerService = playerService;

    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Player>> showPlayers(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                    @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                    @RequestParam(value = "order", required = false) String order,

                                                    @RequestParam(value = "name", required = false) String name,
                                                    @RequestParam(value = "title", required = false) String title,
                                                    @RequestParam(value = "before", required = false) Long before,
                                                    @RequestParam(value = "after", required = false) Long after,
                                                    @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                    @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                    @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                    @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                                    @RequestParam(value = "race", required = false) Race race,
                                                    @RequestParam(value = "profession", required = false) Profession profession,
                                                    @RequestParam(value = "banned", required = false) Boolean banned) {

        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = 3;
        if (order == null) order = "ID";
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

        filterService.filter(players, name, title, before, after, race, profession, banned, minExperience, maxExperience, minLevel, maxLevel);

        try {
            return new ResponseEntity<>(players.subList(pageNumber * pageSize, (pageNumber * pageSize) + pageSize), HttpStatus.OK);
        }
        catch (IndexOutOfBoundsException e) {
            return new ResponseEntity<>(players.subList(pageNumber * pageSize, players.size()), HttpStatus.OK);
        }

    }

    @RequestMapping(value = "count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> countPlayers(@RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "title", required = false) String title,
                                                @RequestParam(value = "before", required = false) Long before,
                                                @RequestParam(value = "after", required = false) Long after,
                                                @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                                @RequestParam(value = "race", required = false) Race race,
                                                @RequestParam(value = "profession", required = false) Profession profession,
                                                @RequestParam(value = "banned", required = false) Boolean banned) {

        List<Player> players = this.playerService.findAll();

        filterService.filter(players, name, title, before, after, race, profession, banned, minExperience, maxExperience, minLevel, maxLevel);

        return new ResponseEntity<>(players.size(), HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> getPlayer(@PathVariable String id) {
        // That's a very bad way to check if id is Integer
        try {
            int intId = Integer.parseInt(id);
            if (intId == 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player player = this.playerService.findById(Long.valueOf(id));
        if (player == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(player, HttpStatus.OK);
    }



}
