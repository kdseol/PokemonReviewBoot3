package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.PokemonType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PokemonRepositoryTests {

    @Autowired
    private PokemonRepository pokemonRepository;

    @Test
    public void PokemonRepository_SaveAll_ReturnSavedPokemon() {
        //Arrange
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type(PokemonType.ELECTRIC)
                .build();
        //Act
        Pokemon savedPokemon = pokemonRepository.save(pokemon);

        //Assert
        assertThat(savedPokemon).isNotNull();
        assertThat(savedPokemon.getId()).isGreaterThan(0);
    }

    @Test
    public void PokemonRepository_GetAll_ReturnMoreThenOnePokemon() {
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type(PokemonType.ELECTRIC).build();
        Pokemon pokemon2 = Pokemon.builder()
                .name("Raichu")
                .type(PokemonType.NORMAL).build();

        pokemonRepository.save(pokemon);
        pokemonRepository.save(pokemon2);

        List<Pokemon> pokemonList = pokemonRepository.findAll();

        Assertions.assertThat(pokemonList).isNotNull();
        Assertions.assertThat(pokemonList.size()).isEqualTo(2);
    }

    @Test
    public void PokemonRepository_FindById_ReturnPokemon() {
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type(PokemonType.ELECTRIC).build();

        pokemonRepository.save(pokemon);

        Pokemon savedPokemon = pokemonRepository
                .findById(pokemon.getId()) //Optional<Pokemon>
                .get();

        assertThat(savedPokemon).isNotNull();
        assertThat(savedPokemon.getName()).isEqualTo("pikachu");
    }

   /* @Test
    public void PokemonRepository_FindByType_ReturnPokemonNotNull() {
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type(PokemonType.ELECTRIC).build();

        pokemonRepository.save(pokemon);

        Pokemon existPokemon = pokemonRepository
                .findByType(pokemon.getType())
                .get();

        System.out.println("PokemonType.ELECTRIC.name() = " + PokemonType.ELECTRIC.name());
        assertThat(existPokemon).isNotNull();
        assertThat(existPokemon.getType()).isEqualTo(PokemonType.ELECTRIC.name());
        System.out.println("PokemonType.ELECTRIC.name() = " + PokemonType.ELECTRIC.name());
    }*/

    @Test
    public void PokemonRepository_UpdatePokemon_ReturnPokemonNotNull() {
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type(PokemonType.ELECTRIC)
                .build();

        pokemonRepository.save(pokemon);

        Pokemon pokemonSave = pokemonRepository.findById(pokemon.getId()).get();
        pokemonSave.setName("Raichu");
        pokemonSave.setType(PokemonType.NORMAL); //Dirty Checking

        assertThat(pokemonSave.getName()).isEqualTo("Raichu");
        assertThat(pokemonSave.getType()).isEqualTo(PokemonType.NORMAL);

        System.out.println("pokemonSave = " + pokemonSave);
    }

    @Test
    public void PokemonRepository_PokemonDelete_ReturnPokemonIsEmpty() {
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type(PokemonType.ELECTRIC)
                .build();

        pokemonRepository.save(pokemon);

        pokemonRepository.deleteById(pokemon.getId());
        Optional<Pokemon> pokemonReturn = pokemonRepository.findById(pokemon.getId());

        assertThat(pokemonReturn).isEmpty();
    }

}