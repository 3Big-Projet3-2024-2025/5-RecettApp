import { User } from "./users"
import { Contest } from "./contest"

export interface Entry {
    id : number,
    user : User,
    contest : Contest
}