package com.pokemonreview.api.service;

import com.pokemonreview.api.dto.PageResponseDto;
import com.pokemonreview.api.dto.PokemonDto;

public interface PokemonService {
    PokemonDto createPokemon(PokemonDto pokemonDto);
    PageResponseDto getAllPokemon(int pageNo, int pageSize);
    PokemonDto getPokemonById(int id);
    PokemonDto updatePokemon(PokemonDto pokemonDto, int id);
    void deletePokemonId(int id);
}