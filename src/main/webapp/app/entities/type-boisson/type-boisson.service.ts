import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITypeBoisson } from 'app/shared/model/type-boisson.model';

type EntityResponseType = HttpResponse<ITypeBoisson>;
type EntityArrayResponseType = HttpResponse<ITypeBoisson[]>;

@Injectable({ providedIn: 'root' })
export class TypeBoissonService {
  public resourceUrl = SERVER_API_URL + 'api/type-boissons';

  constructor(protected http: HttpClient) {}

  create(typeBoisson: ITypeBoisson): Observable<EntityResponseType> {
    return this.http.post<ITypeBoisson>(this.resourceUrl, typeBoisson, { observe: 'response' });
  }

  update(typeBoisson: ITypeBoisson): Observable<EntityResponseType> {
    return this.http.put<ITypeBoisson>(this.resourceUrl, typeBoisson, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeBoisson>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeBoisson[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
