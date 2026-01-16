export class Incident {
    id!: number;
    latitude!: number;
    longitude!: number;
    type!: string;
    description!: string;
    imageUrl!: string;
    status!: string;
    createdAt!: Date;
}