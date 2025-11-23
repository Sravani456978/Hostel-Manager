package uk.ac.tees.mad.hostelmanager.data.mappers

import uk.ac.tees.mad.hostelmanager.data.local.room.MenuItemsEntity
import uk.ac.tees.mad.hostelmanager.data.remote.MenuItemRemote
import uk.ac.tees.mad.hostelmanager.domain.model.Dish
import uk.ac.tees.mad.hostelmanager.domain.model.MenuItem

// Remote → Domain
fun MenuItemRemote.toDomain() = MenuItem(
    day = day,
    breakfast = Dish(breakfast.name, breakfast.imageUrl),
    lunch = Dish(lunch.name, lunch.imageUrl),
    dinner = Dish(dinner.name, dinner.imageUrl)
)

// Domain → Room
fun MenuItem.toEntity() = MenuItemsEntity(
    day = day,
    breakfastName = breakfast.name,
    breakfastImage = breakfast.imageUrl,
    lunchName = lunch.name,
    lunchImage = lunch.imageUrl,
    dinnerName = dinner.name,
    dinnerImage = dinner.imageUrl
)

// Room → Domain
fun MenuItemsEntity.toDomain() = MenuItem(
    day = day,
    breakfast = Dish(breakfastName, breakfastImage),
    lunch = Dish(lunchName, lunchImage),
    dinner = Dish(dinnerName, dinnerImage)
)
