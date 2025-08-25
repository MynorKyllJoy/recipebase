interface Recipe {
    id: string,
    title: string,
    description: string,
    source: string,
    instructions: string[],
    ingredientInfos: string[]
}

export type { Recipe };