export interface ITypeBoisson {
  id?: number;
  code?: number;
  libelle?: string;
}

export class TypeBoisson implements ITypeBoisson {
  constructor(public id?: number, public code?: number, public libelle?: string) {}
}
