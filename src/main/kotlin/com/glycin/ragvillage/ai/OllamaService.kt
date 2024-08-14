package com.glycin.ragvillage.ai

import com.glycin.ragvillage.ai.configuration.OllamaServiceConfiguration
import com.glycin.ragvillage.model.VillagerChatPrompt
import com.glycin.ragvillage.model.VillagerCommandPrompt
import com.glycin.ragvillage.utils.PromptConstants
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.model.ollama.OllamaChatModel
import dev.langchain4j.model.ollama.OllamaStreamingChatModel
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.MemoryId
import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.TokenStream
import dev.langchain4j.service.UserMessage
import org.springframework.stereotype.Service

@Service
class OllamaService(
    config: OllamaServiceConfiguration,
) {
    val villagerAssistant: VillagerAssistant = AiServices.builder(VillagerAssistant::class.java)
        .chatLanguageModel(
            OllamaChatModel.builder()
                .logRequests(config.logRequests)
                .logResponses(config.logResponses)
                .baseUrl(config.url)
                .modelName(config.modelName)
                .temperature(config.temperature)
                .build()
        )
        .streamingChatLanguageModel(
            OllamaStreamingChatModel.builder()
                .logRequests(config.logRequests)
                .logResponses(config.logResponses)
                .baseUrl(config.url)
                .modelName(config.modelName)
                .temperature(config.temperature)
                .build()
        )
        .chatMemoryProvider { memoryId ->
            MessageWindowChatMemory.withMaxMessages(50)
        }
        .build()
}

interface VillagerAssistant {
    @SystemMessage("""You are a helpful assistant and will answer the questions.""")
    fun ask(message: String): String

    @SystemMessage(PromptConstants.COMMAND_PROMPT)
    fun commandVillager(@MemoryId name: String, @UserMessage villagerPrompt: VillagerCommandPrompt): String

    @SystemMessage("""
        ${PromptConstants.VILLAGE_DESCRIPTION}
        You will receive some context in the Context field within the json you will receive.
        You are the villager within little minas morgul as described in the Villager object within the json you will receive. Answer the question within the question field in the json. 
        Answer the question by fully adapting the personality, age and the state of the villager. Your answer should not be longer than 300 characters.
        Do not wrap the answer in a json or in quotation marks.
    """)
    fun chat(@MemoryId name: String, @UserMessage villagerPrompt: VillagerChatPrompt): TokenStream

    @SystemMessage("""
        ${PromptConstants.VILLAGE_DESCRIPTION}
        You are an orc painter called Bobhu Rossh (tribute to Bob Ross) in Little Minas Morgul. You will receive a description of an image. Rewrite that description. 
    """)
    fun describeArt(@MemoryId name: String, @UserMessage description: String): TokenStream

    fun shopKeeper //TODO: Add a shop keeper so i can search for the art i make
}