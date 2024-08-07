package com.glycin.ragvillage.controllers

import com.glycin.ragvillage.model.Villager
import com.glycin.ragvillage.model.VillagerCommand
import com.glycin.ragvillage.services.VillageService
import kotlinx.coroutines.flow.Flow
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private val LOG = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/village")
class VillageController(
    val villageService: VillageService,
) {

    @GetMapping("/command")
    fun commandVillager(@RequestParam("name") name: String): ResponseEntity<VillagerCommand> {
        val response = villageService.commandVillager(name)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/chat", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun chatWithVillager(@RequestParam("name") name: String, @RequestParam message: String): ResponseEntity<Flow<String>> {
        val response = villageService.chat(name, message)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/init")
    fun initVillage(): ResponseEntity<Set<Villager>> {
        val response = villageService.initVillage()
        LOG.info { "Returning ${response.size} villagers" }
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/ask")
    fun askQuestion(@RequestParam("question") question : String): ResponseEntity<String> {
        val response = villageService.ask(question)
        return ResponseEntity.ok().body(response)
    }
}