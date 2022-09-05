package com.game.rest;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

import com.game.rest.comparator.BirthdayComparator;
import com.game.rest.comparator.ExperienceComparator;
import com.game.rest.comparator.IdComparator;
import com.game.rest.comparator.NameComparator;
import com.game.service.FilterService;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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

    @RequestMapping(value = "{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> updatePlayer(@PathVariable String id, @RequestBody Player updatedPlayer) {
        // Check if ID is a number and more than 0.
        try {
            Long idLong = Long.parseLong(id);
            if (idLong <= 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long idLong = Long.parseLong(id);

        Player playerToUpdate = this.playerService.findById(idLong);
        // Check if player exist
        if (playerToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Various checks for null fields. If field is null then we don't update it.
        if (updatedPlayer.getId() == null) updatedPlayer.setId(playerToUpdate.getId());
        if (updatedPlayer.getName() == null) updatedPlayer.setName(playerToUpdate.getName());
        if (updatedPlayer.getTitle() == null) updatedPlayer.setTitle(playerToUpdate.getTitle());
        if (updatedPlayer.getRace() == null) updatedPlayer.setRace(playerToUpdate.getRace());
        if (updatedPlayer.getProfession() == null) updatedPlayer.setProfession(playerToUpdate.getProfession());
        if (updatedPlayer.getBirthday() == null) updatedPlayer.setBirthday(playerToUpdate.getBirthday());
        if (updatedPlayer.isBanned() == null) updatedPlayer.setBanned(playerToUpdate.isBanned());
        if (updatedPlayer.getExperience() == null) updatedPlayer.setExperience(playerToUpdate.getExperience());

        // Checks for experience field. It should be in range from 0 to 10_000_000.
        if (updatedPlayer.getExperience() < 0 || updatedPlayer.getExperience() > 10_000_000) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Check if date is after 1970.
        if (updatedPlayer.getBirthday().getTime() < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        this.playerService.deletePlayer(playerToUpdate);
        updatedPlayer.setId(idLong);
        // Calculation of level and experience.
        updatedPlayer.setLevel((int) Math.floor((Math.sqrt((2500 + (updatedPlayer.getExperience() * 200))) - 50) / 100F));
        updatedPlayer.setUntilNextLevel((50 * (updatedPlayer.getLevel() + 1) * (updatedPlayer.getLevel() + 2)) - updatedPlayer.getExperience());

        this.playerService.savePlayer(updatedPlayer);
        return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> deletePlayer(@PathVariable String id) {
        try {
            Player player = this.playerService.findById(Long.parseLong(id));
            if (Long.parseLong(id) == 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (player == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }



            this.playerService.deletePlayer(player);
            return new ResponseEntity<>(player, HttpStatus.OK);
        }
        catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



}
