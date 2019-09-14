# Foreman
Generator/translator for passing on Weltschmerz's designs to Terra

Foreman acts as seperation between modules functionality

World planner - Weltschmerz
Weltschmerz generates various maps which are then combined into a high level representation of a world map. Weltschmerz is the architect who doesn't get his hands dirty.

World generator - Foreman
Foreman translates Weltschmerz's world map for Terra's consumption and provides Terra with everything it needs. Foreman is the supervisor who reads plans and orders workers around.

World executor - Terra
Terra is the worker who does what he's told, nothing more, nothing less.

This separation of functionality will allow greater flexibility when someone wants to make their own world generator or wants to use the world generator in their own game, etc. It also makes our lives easier by giving us a clearer idea where everything goes semantically.
