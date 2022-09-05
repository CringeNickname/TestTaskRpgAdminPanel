package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FilterService {

    public void filter(List<Player> players, String name, String title, Long before, Long after, Race race, Profession profession, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel) {
        if (name != null) {
            filterByName(name, players);
        }

        if (title != null) {
            filterByTitle(title, players);
        }

        if (race != null) {
            filterByRace(race, players);
        }

        if (profession != null) {
            filterByProfession(profession, players);
        }

        if (banned != null) {
            filterByBanStatus(banned, players);
        }

        if (before != null && after != null) {
            filterByBirthday(before, after, players);
        }

        if (before != null && after == null) {
            filterByBirthdayBefore(before, players);
        }

        if (before == null && after != null) {
            filterByBirthdayAfter(after, players);
        }

        if (minExperience != null && maxExperience != null) {
            filterByExperience(minExperience, maxExperience, players);
        }

        if (minExperience != null && maxExperience == null) {
            filterByMinExperience(minExperience, players);
        }

        if (minExperience == null && maxExperience != null) {
            filterByMaxExperience(maxExperience, players);
        }

        if (minLevel != null && maxLevel != null) {
            filterByLevel(minLevel, maxLevel, players);
        }

        if (minLevel != null && maxLevel == null) {
            filterByMinLevel(minLevel, players);
        }

        if (minLevel == null && maxLevel != null) {
            filterByMaxLevel(maxLevel, players);
        }
    }

    private static void filterByName(String name, List<Player> players) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getName().contains(name)) {
                filteredPlayers.add(player);
            }
        }
        players.retainAll(filteredPlayers);
    }

    private static void filterByTitle(String title, List<Player> players) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getTitle().contains(title)) {
                filteredPlayers.add(player);
            }
        }
        players.retainAll(filteredPlayers);
    }

    private static void filterByRace(Race race, List<Player> players) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getRace().equals(race)) {
                filteredPlayers.add(player);
            }
        }
        players.retainAll(filteredPlayers);
    }

    private static void filterByProfession(Profession profession, List<Player> players) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getProfession().equals(profession)) {
                filteredPlayers.add(player);
            }
        }
        players.retainAll(filteredPlayers);
    }

    private static void filterByBanStatus(Boolean banned, List<Player> players) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.isBanned().equals(banned)) {
                filteredPlayers.add(player);
            }
        }
        players.retainAll(filteredPlayers);
    }

    private static void filterByBirthday(Long before, Long after, List<Player> players) {
        Date lastDate = new Date(before);
        Date firstDate = new Date(after);
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getBirthday().before(lastDate) && player.getBirthday().after(firstDate)) {
                filteredPlayers.add(player);
            }
        }
        players.retainAll(filteredPlayers);
    }

    private static void filterByBirthdayBefore(Long before, List<Player> players) {
        Date lastDate = new Date(before);
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getBirthday().before(lastDate)) {
                filteredPlayers.add(player);
            }
        }
        players.retainAll(filteredPlayers);
    }

    private static void filterByBirthdayAfter(Long after, List<Player> players) {
        Date firstDate = new Date(after);
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getBirthday().after(firstDate)) {
                filteredPlayers.add(player);
            }
        }
        players.retainAll(filteredPlayers);
    }

    private static void filterByExperience(Integer minExperience, Integer maxExperience, List<Player> players) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getExperience() >= minExperience && player.getExperience() <= maxExperience) {
                filteredPlayers.add(player);
            }
        }
        players.retainAll(filteredPlayers);
    }

    private static void filterByMinExperience(Integer minExperience, List<Player> players) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getExperience() >= minExperience) {
                filteredPlayers.add(player);
            }
        }
        players.retainAll(filteredPlayers);
    }

    private static void filterByMaxExperience(Integer maxExperience, List<Player> players) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getExperience() <= maxExperience) {
                filteredPlayers.add(player);
            }
        }
        players.retainAll(filteredPlayers);
    }

    private static void filterByLevel(Integer minLevel, Integer maxLevel, List<Player> players) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getLevel() >= minLevel && player.getLevel() <= maxLevel) {
                filteredPlayers.add(player);
            }
        }
        players.retainAll(filteredPlayers);
    }

    private static void filterByMinLevel(Integer minLevel, List<Player> players) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getLevel() >= minLevel) {
                filteredPlayers.add(player);
            }
        }
        players.retainAll(filteredPlayers);
    }

    private static void filterByMaxLevel(Integer maxLevel, List<Player> players) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getLevel() <= maxLevel) {
                filteredPlayers.add(player);
            }
        }
        players.retainAll(filteredPlayers);
    }
}
