package com.lab.esh1n.weather.weather.mapper

import com.lab.esh1n.data.cache.entity.WeatherEntity
import com.lab.esh1n.weather.domain.base.Mapper
import com.lab.esh1n.weather.domain.events.mapper.EpochDateMapper
import com.lab.esh1n.weather.weather.WeatherModel


class EventModelMapper : Mapper<WeatherEntity, WeatherModel>() {

    private val dateMapper = EpochDateMapper()
    override fun map(source: WeatherEntity): WeatherModel {
        return WeatherModel(
                id = source.id,
                type = source.type,
                repositoryName = extractRepoName(source.repositoryName),
                actorName = source.actorName,
                actorAvatar = source.actorAvatar ?: "",
                repositoryLink = prepareRepositoryLink(source.repositoryName),
                createdDate = dateMapper.mapInverse(source.createdDate)
        )
    }

    private fun extractRepoName(repoPath: String): String {
        return repoPath.substring(repoPath.lastIndexOf('/') + 1)
    }

    private fun prepareRepositoryLink(repositoryName: String): String {
        return "$GITBUB_URL$repositoryName"
    }

    companion object {
        const val GITBUB_URL = "https://github.com/"
    }
}