import { Component, Inject, OnInit } from '@angular/core';
import { ItemRequestDTO } from '../../components/items/item.model';
import { ItemService } from '../../services/item.service';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-item-dialog',
  templateUrl: './item-dialog.component.html',
  styleUrls: ['./item-dialog.component.css']
})
export class ItemDialogComponent implements OnInit {
  item: ItemRequestDTO = {
    itemsName: '',
    itemsCode: '',
    stock: 0,
    price: 0,
    isAvailable: 1
  };
  isEditMode = false;
  itemId: number | null = null;
  errorMessage: string = '';

  constructor(
    private itemService: ItemService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.itemId = +id;
      this.itemService.getItemById(this.itemId).subscribe(item => {
        this.item = item;
      });
    } else {
      this.itemService.generateItemCode().subscribe(newCode => {
        this.item.itemsCode = newCode;
      });
    }
  }

  onSubmit(): void {
    if (this.isEditMode && this.itemId !== null) {
      this.itemService.updateItem(this.itemId, this.item).pipe(
        catchError((error: HttpErrorResponse) => {
          this.errorMessage = this.splitErrorMessage(error.error.message);
          return throwError(error);
        })
      ).subscribe(() => {
        this.router.navigate(['/items']);
      });
    } else {
      this.itemService.addItem(this.item).pipe(
        catchError((error: HttpErrorResponse) => {
          this.errorMessage = this.splitErrorMessage(error.error.message);
          return throwError(error);
        })
      ).subscribe(() => {
        this.router.navigate(['/items']);
      });
    }
  }

  onCancel(): void {
    this.router.navigate(['/items']);
  }

  private splitErrorMessage(errorMessage: string): string {
    const parts = errorMessage.split('"');
    return parts.length > 1 ? parts[1] : errorMessage;
  }
}
